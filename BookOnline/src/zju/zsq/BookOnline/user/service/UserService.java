package zju.zsq.BookOnline.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import zju.zsq.BookOnline.user.dao.UserDao;
import zju.zsq.BookOnline.user.domain.User;
import zju.zsq.BookOnline.user.service.exception.UserException;
import zju.zsq.commons.CommonUtils;
import zju.zsq.mail.Mail;
import zju.zsq.mail.MailUtils;

/**
 * 用户模块业务层
 * @author zhushiqing
 *
 */
public class UserService {
	private UserDao userDao = new UserDao();
	
	
	//校验老密码
	public void updatePassword(String uid,String oldPass,String newPass) throws UserException{
		
		try {
			/**
			 * 1.校验老密码
			 */
			boolean bool = userDao.findByUidAndPassword(uid,oldPass);
			/**
			 * 2.如果老密码错误
			 */
			if(!bool){
				throw new UserException("老密码错误！");
			}else{
				userDao.updatePassword(uid, newPass);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public User Login(User user){
		String loginname = user.getLoginname();
		String loginpass = user.getLoginpass();
		
		try {
			return userDao.findByLoginnameAndLoginpass(loginname, loginpass);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 激活功能
	 * @param code
	 * @throws UserException 
	 */
	public void activation (String code) throws SQLException, UserException{
		User user = userDao.findByCode(code);
		if(user == null)
			throw new UserException("无效的激活码");
		if(user.isStatus())
			throw new UserException("您已经激活过了，请不要再次激活！");
		userDao.updateStatus(code, true);
	}
	
	/**
	 * 用户名校验
	 * @param loginname
	 * @return
	 */
	public boolean ajaxValidateLoginname(String loginname){
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * email校验
	 * @param email
	 * @return
	 */
	public boolean ajaxValidateEmail(String email){
		try {
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void regist(User user){
		/**
		 * 1.补齐数据
		 */
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
		/**
		 * 2.向数据库插入
		 */
		
		
		try {
			userDao.add(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		/**
		 * 3.发邮件通知用户
		 * 3.1等于邮箱
		 */
		
		//把email_template.properties加载到pop里面来
		Properties pop = new Properties();
		try {
			pop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}	
		String host = pop.getProperty("host");//服务器主机名
		String name = pop.getProperty("username"); //用户名
		String pass = pop.getProperty("password"); //密码
		Session session = MailUtils.createSession(host, name, pass);
		
		/**
		 * 3.2创建Mail对象
		 */
		
	
		
		
		String from = pop.getProperty("from");
		String to = user.getEmail();
		String subject = pop.getProperty("subject");
		//MessageFormat.format 方法会把第一个参数中的{0} 替换成第二个参数 
		String content = MessageFormat.format(pop.getProperty("content"), user.getActivationCode());
		Mail mail = new Mail(from, to, subject, content);
		/**
		 * 3.3 发送邮件
		 */
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
}
