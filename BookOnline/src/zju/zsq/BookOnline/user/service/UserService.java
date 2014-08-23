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
 * �û�ҵ���߼���
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
		 * 1ͨ���������ѯ�û�
		 * 2���userΪnull˵������Ч�����룬�׳��쳣�������쳣��Ϣ
		 * 3�鿴�û�״̬�Ƿ�Ϊtrue������ǣ��׳��쳣�����쳣��Ϣ���벻Ҫ�ٴμ��
		 * 4�޸��û�״̬Ϊtrue
		 */
		
		try {
			User user = userDao.findByCode(code);
			if (user == null)
				throw new UserException("��Ч�ļ�����");
			if(user.isStatus())
				throw new UserException("���Ѿ�������ˣ�");
			userDao.updateUserStatus(user.getUid(), true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * �û���ע��У��
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
	 * EmailУ��
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
		
		//���ݵĲ���
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
		
		//�����ݿ����
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
		
		//���ʼ�
		//��¼�ʼ����������õ�������
		String host = pop.getProperty("host");
		String name = pop.getProperty("username");
		String pass = pop.getProperty("password");
		Session session = MailUtils.createSession( host, name, pass);
		
		//����mail����
		String from = pop.getProperty("from");
		String to = user.getEmail();
		String subject = pop.getProperty("suject");
		//�滻ռλ��
		//���� MessageFromat("hello,{0},{1}","����","ȥ���ɣ�")=hello����������ȥ���ɣ�
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