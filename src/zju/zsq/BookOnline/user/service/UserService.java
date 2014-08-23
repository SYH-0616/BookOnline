package zju.zsq.BookOnline.user.service;

import java.sql.SQLException;

import zju.zsq.BookOnline.user.dao.UserDao;

/**
 * 用户模块业务层
 * @author zhushiqing
 *
 */
public class UserService {
	private UserDao userDao = new UserDao();
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
}
