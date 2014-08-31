package zju.zsq.BookOnline.cart.service;

import java.sql.SQLException;
import java.util.List;

import zju.zsq.BookOnline.cart.dao.CartItemDao;
import zju.zsq.BookOnline.cart.domain.CartItem;
import zju.zsq.commons.CommonUtils;

public class CartItemService {

	private CartItemDao cartItemDao = new CartItemDao();

	
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
