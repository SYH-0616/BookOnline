package zju.zsq.BookOnline.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import zju.zsq.jdbc.TxQueryRunner;



/**
 * 用户模块持久层
 * @author zhushiqing
 *
 */
public class UserDao {
	private QueryRunner qr = new TxQueryRunner();
	
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
}
