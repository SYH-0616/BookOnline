package zju.zsq.BookOnline.order.domain;

import java.util.List;

import zju.zsq.BookOnline.user.domain.User;

public class Order {
	private String oid;//主键
	private String ordertiem;
	private double total;//总计
	private int status;//订单状态 1.未付款 2.已付款，未发货 3.已发货，未确认收货 4.确认收货了，交易成功 5.已取消 只有未付款才能取消
	private String address;
	private User owner;
	private List<OrderItem> orderItemList;
	
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getOrdertiem() {
		return ordertiem;
	}
	public void setOrdertiem(String ordertiem) {
		this.ordertiem = ordertiem;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	
	
}
