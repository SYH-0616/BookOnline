package zju.zsq.BookOnline.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import zju.zsq.BookOnline.book.domain.Book;
import zju.zsq.BookOnline.cart.domain.CartItem;
import zju.zsq.BookOnline.user.domain.User;
import zju.zsq.commons.CommonUtils;
import zju.zsq.jdbc.TxQueryRunner;

public class CartItemDao {
	private QueryRunner qr = new TxQueryRunner();

	public CartItem findByUidAndBid(String uid, String bid) {
		String sql = "select * from t_cartitem where uid = ? and bid = ?";
		try {
			Map<String, Object> map = qr.query(sql, new MapHandler(), uid, bid);
			CartItem cartItem = toCartItem(map);
			return cartItem;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void updateQuantity(String cartItemId, int quantity)
			throws SQLException {
		String sql = "update t_cartitem set quantity = ? where cartItemId = ?";
		qr.update(sql, quantity, cartItemId);
	}

	public void addCartItem(CartItem cartItem) throws SQLException {
		String sql = "insert into t_cartitem (cartItemId,quantity,bid,uid) values (?,?,?,?)";
		Object[] params = { cartItem.getCartItemId(), cartItem.getQuantity(),
				cartItem.getBook().getBid(), cartItem.getUser().getUid() };

		qr.update(sql, params);
	}

	/**
	 * 把一个map映射成cartItem
	 * 
	 * @param map
	 * @return
	 */
	private CartItem toCartItem(Map<String, Object> map) {

		if (map == null || map.size() == 0)
			return null;
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = CommonUtils.toBean(map, User.class);

		cartItem.setBook(book);
		cartItem.setUser(user);
		return cartItem;
	}

	private List<CartItem> toCartItemList(List<Map<String, Object>> mapList) {
		List<CartItem> cartItemList = new ArrayList<CartItem>();

		for (Map<String, Object> map : mapList) {
			CartItem cartItem = toCartItem(map);
			cartItemList.add(cartItem);
		}

		return cartItemList;
	}

	/**
	 * 通过用户查询用户的购物车
	 * 
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> findByUser(String uid) throws SQLException {
		String sql = "select * from t_cartitem c, t_book b where c.bid = b.bid and uid = ? order by c.orderBy";
		List<Map<String, Object>> maplist = qr.query(sql, new MapListHandler(),
				uid);

		return toCartItemList(maplist);

	}
}
