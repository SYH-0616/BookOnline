package zju.zsq.BookOnline.admin.admin.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import zju.zsq.BookOnline.admin.admin.domain.Admin;
import zju.zsq.jdbc.TxQueryRunner;

public class AdminDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 通过管理员名跟密码查询
	 * @param adminname
	 * @param adminpwd
	 * @return
	 * @throws SQLException
	 */
	public Admin find(String adminname,String adminpwd) throws SQLException{
		String sql = "select * from t_admin where adminname = ? and adminpwd = ?";
		Admin admin = qr.query(sql, new BeanHandler<Admin>(Admin.class),adminname,adminpwd);
		return admin;
	}
}
