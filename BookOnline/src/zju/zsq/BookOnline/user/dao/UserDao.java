package zju.zsq.BookOnline.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import zju.zsq.BookOnline.user.domain.User;
import zju.zsq.jdbc.TxQueryRunner;



/**
 * 用户模块持久层
 * @author zhushiqing
 *
 */
public class UserDao {
	private QueryRunner qr = new TxQueryRunner();
	
	//按照uid跟password查询
	public boolean findByUidAndPassword(String uid,String loginpass) throws SQLException{
		String sql = "select count(1) from t_user where uid = ? and loginpass = ?";
		Number number = (Number) qr.query(sql, new ScalarHandler(),uid,loginpass);
		return number.intValue()>0;
	}
	
	public void updatePassword(String uid,String loginpass) throws SQLException{
		String sql = "update t_user set loginpass = ? where uid = ?";
		qr.update(sql,loginpass,uid);
	}
	
	//按用户名跟密码查询
	public User findByLoginnameAndLoginpass(String loginname , String loginpass) throws SQLException{
		String sql = "select * from t_user where loginname = ? and loginpass = ?";
		return qr.query(sql, new BeanHandler<User>(User.class),loginname,loginpass);
	}
	/**
	 * 校验用户名是否注册
	 * @param loginname
	 * @return
	 * @throws SQLException 
	 */
	public boolean ajaxValidateLoginname(String loginname) throws SQLException{
		String sql = "select count(1) from t_user where loginname = ?";
		Number number = (Number)qr.query(sql, new ScalarHandler(),loginname);
		
		return number.intValue()==0;//如果一个用户名都不存在，代表没注册过
	}
	/**
	 * 校验Email是否注册
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateEmail(String email) throws SQLException{
		String sql = "select count(1) from t_user where loginname = ?";
		Number number = (Number)qr.query(sql, new ScalarHandler(),email);
		
		return number.intValue()==0;//如果一个Email都不存在，代表没注册过
	}
	
	public void add(User user) throws SQLException{
		String sql = "insert into t_user values(?,?,?,?,?,?)";
		Object[] params = {user.getUid(),user.getLoginname(),user.getLoginpass(),user.getEmail(),user.isStatus(),user.getActivationCode()};
		qr.update(sql,params);
	}
	
	//根据激活码查询用户
	public User findByCode(String code){
		String sql = "select * from t_user where activationCode = ?";
		try {
			return qr.query(sql, new BeanHandler<User>(User.class), code);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//修改用户方法
	public void updateStatus(String activationCode,boolean status) throws SQLException{
		String sql = "update t_user set status = ? where  activationCode = ?";
		qr.update(sql, status, activationCode);
	}
	
}
