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
 * �û�ģ��ҵ���
 * @author zhushiqing
 *
 */
public class UserService {
	private UserDao userDao = new UserDao();
	
	
	//У��������
	public void updatePassword(String uid,String oldPass,String newPass) throws UserException{
		
		try {
			/**
			 * 1.У��������
			 */
			boolean bool = userDao.findByUidAndPassword(uid,oldPass);
			/**
			 * 2.������������
			 */
			if(!bool){
				throw new UserException("���������");
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
	 * �����
	 * @param code
	 * @throws UserException 
	 */
	public void activation (String code) throws SQLException, UserException{
		User user = userDao.findByCode(code);
		if(user == null)
			throw new UserException("��Ч�ļ�����");
		if(user.isStatus())
			throw new UserException("���Ѿ�������ˣ��벻Ҫ�ٴμ��");
		userDao.updateStatus(code, true);
	}
	
	/**
	 * �û���У��
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
	 * emailУ��
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
		 * 1.��������
		 */
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
		/**
		 * 2.�����ݿ����
		 */
		
		
		try {
			userDao.add(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		/**
		 * 3.���ʼ�֪ͨ�û�
		 * 3.1��������
		 */
		
		//��email_template.properties���ص�pop������
		Properties pop = new Properties();
		try {
			pop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}	
		String host = pop.getProperty("host");//������������
		String name = pop.getProperty("username"); //�û���
		String pass = pop.getProperty("password"); //����
		Session session = MailUtils.createSession(host, name, pass);
		
		/**
		 * 3.2����Mail����
		 */
		
	
		
		
		String from = pop.getProperty("from");
		String to = user.getEmail();
		String subject = pop.getProperty("subject");
		//MessageFormat.format ������ѵ�һ�������е�{0} �滻�ɵڶ������� 
		String content = MessageFormat.format(pop.getProperty("content"), user.getActivationCode());
		Mail mail = new Mail(from, to, subject, content);
		/**
		 * 3.3 �����ʼ�
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
