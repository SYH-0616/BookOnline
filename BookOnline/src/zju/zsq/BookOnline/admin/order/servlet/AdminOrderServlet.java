package zju.zsq.BookOnline.admin.order.servlet;

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

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/admin/AdminOrderServlet")
public class AdminOrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private OrderService orderService = new OrderService();

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

	public String findAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException {
		// 1.得到pc，如果页面传递，则使用页面的，如果页面没有传递，pc=1
		// 2.得到url
		// 3.获取查询条件，获取uid
		// 4.使用pc与cid 调用findByCategory得到pageBean
		// 5.给PageBean
		int pc = getPc(request);
		String url = getUrl(request);
		PageBean<Order> pb = orderService.findAll(pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	public String findByStatus(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException {
		// 1.得到pc，如果页面传递，则使用页面的，如果页面没有传递，pc=1
		// 2.得到url
		// 3.获取查询条件，获取uid
		// 4.使用pc与cid 调用findByCategory得到pageBean
		// 5.给PageBean
		int pc = getPc(request);
		String url = getUrl(request);
		
		int status = Integer.parseInt(request.getParameter("status"));
		
		PageBean<Order> pb = orderService.findByStatus(status,pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * 查看订单详细信息
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		Order order = orderService.load(oid);
		req.setAttribute("order", order);
		String btn = req.getParameter("btn");//btn说明了用户点击哪个超链接来访问本方法的
		req.setAttribute("btn", btn);
		return "/adminjsps/admin/order/desc.jsp";
	}
	
	/**
	 * 取消订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String cancel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		/*
		 * 校验订单状态
		 */
		int status = orderService.findStatus(oid);
		if(status != 1) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能取消！");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);//设置状态为取消！
		req.setAttribute("code", "success");
		req.setAttribute("msg", "您的订单已取消，您不后悔吗！");
		return "f:/adminjsps/msg.jsp";		
	}
	
	/**
	 * 发货功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deliver(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {  
		String oid = req.getParameter("oid");
		/*
		 * 校验订单状态
		 */
		int status = orderService.findStatus(oid);
		if(status != 2) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能发货！");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 3);//设置状态为取消！
		req.setAttribute("code", "success");
		req.setAttribute("msg", "您的订单已发货，请查看物流，马上确认吧！");
		return "f:/adminjsps/msg.jsp";		
	}

}
