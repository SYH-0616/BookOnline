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
	
	//加载多个cartItems
	public String loadCartItems(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//获取cartItemids
		String cartItemIds = req.getParameter("cartItemIds");
		//获取total
		double total = Double.parseDouble(req.getParameter("total"));
		List<CartItem> list = cartItemService.loadCartItems(cartItemIds);
		req.setAttribute("cartItemList", list);
		req.setAttribute("total", total);
		return "f:/jsps/cart/showitem.jsp";
		 
	}
	
	
	public String updateQuantity(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		 String cartItemId = req.getParameter("cartItemId");
		 int quantity = Integer.parseInt(req.getParameter("quantity"));
		 CartItem cartItem = cartItemService.updateQuantity(cartItemId, quantity);
		 
		 //给客户端返回一个json对象
		 StringBuilder sb = new StringBuilder("{");
		 sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
		 sb.append(",");
		 sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
		 sb.append("}");
		 System.out.println(sb);
		 resp.getWriter().print(sb);
		 return null;
	}
	
	
	/**
	 * 批量删除
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String batchDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cartItemIds = req.getParameter("cartItemIds");
		cartItemService.batchDelete(cartItemIds);
		return myCart(req, resp);
	}
	
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
