package zju.zsq.BookOnline.cart.service;

import java.sql.SQLException;
import java.util.List;

import zju.zsq.BookOnline.cart.dao.CartItemDao;
import zju.zsq.BookOnline.cart.domain.CartItem;
import zju.zsq.commons.CommonUtils;

public class CartItemService {

	private CartItemDao cartItemDao = new CartItemDao();

	//加载多个cartItem
	public List<CartItem> loadCartItems(String cartItemIds){
		try {
			return cartItemDao.loadCartItems(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 修改条目数量
	 */
	public CartItem updateQuantity(String cartItemId,int quantity){
		try{
			cartItemDao.updateQuantity(cartItemId, quantity);
			return cartItemDao.findByCartItemId(cartItemId);
			
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	//删除
	public void batchDelete(String cartItemIds){
		try {
			cartItemDao.batchDelete(cartItemIds);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加购物车
	 * @param cartItem
	 */
	public void addCartItem(CartItem cartItem) {

		try {
			CartItem _cartItem = cartItemDao.findByUidAndBid(cartItem.getUser()
					.getUid(), cartItem.getBook().getBid());
			if (_cartItem == null) {// 如果原来没这条目，则添加
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.addCartItem(cartItem);
			} else {//如果有，则修改数量
				int quantity = cartItem.getQuantity() + _cartItem.getQuantity();
				cartItemDao.updateQuantity(_cartItem.getCartItemId(), quantity);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 我的购物车
	 * 
	 * @param uid
	 * @return
	 */
	public List<CartItem> myCart(String uid) {
		try {
			return cartItemDao.findByUser(uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
