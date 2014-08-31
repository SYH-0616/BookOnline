package zju.zsq.BookOnline.order.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zju.zsq.BookOnline.order.domain.Order;
import zju.zsq.BookOnline.order.service.OrderService;
import zju.zsq.BookOnline.pager.PageBean;
import zju.zsq.BookOnline.user.domain.User;
import zju.zsq.servlet.BaseServlet;

@WebServlet("/OrderServlet")
public class OrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private OrderService orderService;

	// 获取当前页码
	private int getPc(HttpServletRequest request) {
		int pc = 1;
		String param = request.getParameter("pc");
		if (param != null && !param.trim().isEmpty()) {
			try {
				pc = Integer.parseInt(param);
			} catch (RuntimeException e) {

			}
		}
		return pc;
	}

	// 截取URL，页面中的分页导航需要使用他作为超链接的目标
	private String getUrl(HttpServletRequest request) {
		String url = request.getRequestURI() + "?" + request.getQueryString();
		//
		int index = url.indexOf("&pc=");
		if (index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}
	
	/**
	 * 按分类查询
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException 
	 */
	public String myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//1.得到pc，如果页面传递，则使用页面的，如果页面没有传递，pc=1
		//2.得到url
		//3.获取查询条件，获取uid
		//4.使用pc与cid 调用findByCategory得到pageBean
		//5.给PageBean
		int pc = getPc(request);
		String url = getUrl(request);
		User user = (User)request.getSession().getAttribute("UserSession");
		PageBean<Order> pb = orderService.myOrders(user.getUid(), pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/jsps/order/list.jsp";
	}

}
