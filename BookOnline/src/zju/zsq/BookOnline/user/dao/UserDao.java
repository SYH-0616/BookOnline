package zju.zsq.BookOnline.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import zju.zsq.BookOnline.user.domain.User;
import zju.zsq.jdbc.TxQueryRunner;
/**
 *  �û��־ò�
 * @author zhushiqing
 *
 */
public class UserDao {
	private QueryRunner qr = new TxQueryRunner();
	//���û����������ѯ
	/*
	 * 
	 */
	public User findByLoginnameAndLoginpass(String loginname,String loginpass) throws SQLException{
		String sql = "SELECT * from t_user where loginname = ? and loginpass = ?";
		return qr.query(sql, new BeanHandler<User>(User.class),loginname,loginpass);
	}
	
	/**
	 * У���û����Ƿ�ע��
	 * @param loginname
	 * @return
	 * @throws SQLException 
	 */
	public boolean ajaxValidateLonginname(String loginname) throws SQLException{
		String sql = "select count(1) from t_user where loginname = ?";
		Number number = (Number)qr.query(sql, new ScalarHandler(),loginname);
		return number.intValue() == 0;
	}
	/**
	 * У��Email�Ƿ�ע��
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateEmail(String email) throws SQLException{
		String sql = "select count(1) from t_user where email = ?";
		Number number = (Number)qr.query(sql, new ScalarHandler(),email);
		return number.intValue() == 0;
	}
	
	public void addUser(User user) throws SQLException{
		String sql = "insert into t_user values(?,?,?,?,?,?)";
		Object[] params = {user.getUid(),user.getLoginname(),user.getLoginpass(),
				user.getEmail(),user.isStatus(),user.getActivationCode()};
		qr.update(sql,params);
	}
	
	/**
	 * ͨ���������ѯ�û�
	 * @param code
	 * @return
	 * @throws SQLException 
	 */
	public User findByCode(String code) throws SQLException{
		String sql = "select * from t_user where activationCode = ?";
		return qr.query(sql, new BeanHandler<User>(User.class),code);
	}
	
	/**
	 * 
	 * @param uid
	 * @param status
	 * @throws SQLException 
	 */
	public void updateUserStatus(String uid, boolean status) throws SQLException{
		String sql =  "update t_user set status = ? where uid = ?";
		qr.update(sql, status,uid);
	}
}
