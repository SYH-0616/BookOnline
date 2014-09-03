package zju.zsq.BookOnline.order.service;

import java.sql.SQLException;

import zju.zsq.BookOnline.order.dao.OrderDao;
import zju.zsq.BookOnline.order.domain.Order;
import zju.zsq.BookOnline.pager.PageBean;
import zju.zsq.jdbc.JdbcUtils;

public class OrderService {
	private OrderDao orderDao = new OrderDao();
	
	/**
	 * 查询订单状态
	 * @param oid
	 * @return
	 */
	public int findStatus(String oid) {
		try {
			return orderDao.findStatus(oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 修改订单状态
	 * @param oid
	 * @param status
	 */
	public void updateStatus(String oid,int status) {
		try {
			orderDao.updateStatus(oid,status);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public Order load(String oid){
		try {
			JdbcUtils.beginTransaction();
			Order order = orderDao.load(oid);
			JdbcUtils.commitTransaction();
			return order;
		} catch (SQLException e) {
			try{
				JdbcUtils.rollbackTransaction();
			}catch(SQLException e1){}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 创建订单
	 * @param uid
	 * @param pc
	 * @return
	 */
	public void createOrder(Order order) {
		try {
			JdbcUtils.beginTransaction();
			orderDao.add(order);
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	/**
	 * 我的订单
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<Order> myOrders(String uid, int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findByUser(uid, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	public PageBean<Order> findAll(int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findAll( pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 按状态查询
	 * @param status
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findByStatus(int status,int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findByStatus( status,pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	
}
