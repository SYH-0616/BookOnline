package zju.zsq.BookOnline.cart.domain;

import java.math.BigDecimal;

import zju.zsq.BookOnline.book.domain.Book;
import zju.zsq.BookOnline.user.domain.User;

public class CartItem {

	private String cartItemId;//主键
	private int quantity;//数量
	private Book book;//
	private User user;//所属用户
	
	
	//添加小计方法，不会有误差，要求必须使用String类型构造器
	public double getSubtotal(){
		BigDecimal b1 = new BigDecimal(book.getCurrPrice() + "");
		BigDecimal b2 = new BigDecimal(quantity + "");
		BigDecimal b3 = b1.multiply(b2);
		return b3.doubleValue();
	}
	
	public String getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
