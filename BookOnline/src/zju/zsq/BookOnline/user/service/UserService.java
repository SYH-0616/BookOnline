package zju.zsq.BookOnline.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.management.RuntimeErrorException;

import org.apache.commons.dbutils.handlers.ScalarHandler;

import zju.zsq.BookOnline.user.dao.UserDao;
import zju.zsq.BookOnline.user.domain.User;
import zju.zsq.BookOnline.user.service.exception.UserException;
import zju.zsq.commons.CommonUtils;
import zju.zsq.mail.Mail;
import zju.zsq.mail.MailUtils;

/**
 * 用户业务逻辑层
 * @author zhushiqing
 *
 */
public class UserService {
	private UserDao userDao = new UserDao();
	
	/**
	 * 
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	public User login(User user) throws SQLException{
		return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
		
	}
	
	public void activaction(String code) throws UserException{
		/**
		 * 1通过激活码查询用户
		 * 2如果user为null说明是无效激活码，抛出异常，给出异常信息
		 * 3查看用户状态是否为true，如果是，抛出异常给出异常信息（请不要再次激活）
		 * 4修改用户状态为true
		 */
		
		try {
			User user = userDao.findByCode(code);
			if (user == null)
				throw new UserException("无效的激活码");
			if(user.isStatus())
				throw new UserException("您已经激活过了！");
			userDao.updateUserStatus(user.getUid(), true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * 用户名注册校验
	 * @param loginname
	 * @return
	 */
	public boolean ajaxValidateLonginname(String loginname){
		try {
			return userDao.ajaxValidateLonginname(loginname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * Email校验
	 * @param email
	 * @return
	 */
	public boolean ajaxValidateEmail(String email){
		try {
			return userDao.ajaxValidateLonginname(email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public void regist(User user){
		
		//数据的补齐
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
		
		//向数据库插入
		try {
			userDao.addUser(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		Properties pop = new Properties();
		try {
			pop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		
		//发邮件
		//登录邮件服务器，得到主机名
		String host = pop.getProperty("host");
		String name = pop.getProperty("username");
		String pass = pop.getProperty("password");
		Session session = MailUtils.createSession( host, name, pass);
		
		//创建mail对象
		String from = pop.getProperty("from");
		String to = user.getEmail();
		String subject = pop.getProperty("suject");
		//替换占位符
		//例如 MessageFromat("hello,{0},{1}","张三","去死吧！")=hello，张三，你去死吧！
		String content = MessageFormat.format(pop.getProperty("content"), user.getActivationCode()) ;
		
		Mail mail = new Mail(from,to,subject,content);
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
				
		
	}
	
	
}