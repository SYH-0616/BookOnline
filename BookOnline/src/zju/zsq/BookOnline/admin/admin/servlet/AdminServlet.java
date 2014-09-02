package zju.zsq.BookOnline.admin.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zju.zsq.BookOnline.admin.admin.domain.Admin;
import zju.zsq.BookOnline.admin.admin.service.AdminService;
import zju.zsq.commons.CommonUtils;
import zju.zsq.servlet.BaseServlet;

@WebServlet("/AdminServlet")
public class AdminServlet extends BaseServlet{
	private AdminService adminService = new AdminService();
	
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1.封装表单数据
		Admin form = CommonUtils.toBean(req.getParameterMap(), Admin.class);
		//2.Admin
		Admin admin = adminService.login(form);
		if(admin==null){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "用户名或者密码为空");
			return "f:/adminjsps/login.jsp";
		}else{
			req.getSession().setAttribute("admin", admin);
			return "f:/adminjsps/admin/index.jsp";
		}
	}
}
