package zju.zsq.BookOnline.cart.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zju.zsq.BookOnline.book.domain.Book;
import zju.zsq.BookOnline.cart.domain.CartItem;
import zju.zsq.BookOnline.cart.service.CartItemService;
import zju.zsq.BookOnline.user.domain.User;
import zju.zsq.commons.CommonUtils;
import zju.zsq.servlet.BaseServlet;


@WebServlet("/CartItemServlet")
public class CartItemServlet extends BaseServlet{
	
	private CartItemService cartItemService = new CartItemService();
	
	public String add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//封装表单数据到cartItem,bid,quantity
		Map map = req.getParameterMap();
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = (User)req.getSession().getAttribute("UserSession");
		cartItem.setBook(book);
		cartItem.setUser(user);
		
		cartItemService.addCartItem(cartItem);
		
		return myCart(req, resp);
	}
	
	
	
	//我的购物车
	public String myCart(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User)req.getSession().getAttribute("UserSession");
		String uid = user.getUid();
		
		List<CartItem> cartList = cartItemService.myCart(uid);
		
		req.setAttribute("cartItemList", cartList);
		return "f:/jsps/cart/list.jsp";
		
	}
}	
