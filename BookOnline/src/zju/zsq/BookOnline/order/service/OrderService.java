package zju.zsq.BookOnline.order.service;

import java.sql.SQLException;

import zju.zsq.BookOnline.order.dao.OrderDao;
import zju.zsq.BookOnline.order.domain.Order;
import zju.zsq.BookOnline.pager.PageBean;
import zju.zsq.jdbc.JdbcUtils;

public class OrderService {
	private OrderDao orderDao;
	
	/**
	 * 我的订单
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> myOrders(String uid,int pc) throws SQLException{
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findByUser(uid, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally{
			JdbcUtils.rollbackTransaction();
		}
	}
}
