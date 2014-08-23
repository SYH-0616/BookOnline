package zju.zsq.BookOnline.user.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zju.zsq.BookOnline.user.service.UserService;
import zju.zsq.servlet.BaseServlet;


/**
 * 用户模块WEB层
 * @author zhushiqing
 *
 */
public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	public  String regist(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		
		System.out.println("regist....");
		return null;
	}
	/**
	 * ajax用户名是否注册校验
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public  String ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		//1.得到用户名
		String loginname = req.getParameter("loginname");
		//2.通过service得到校验交过
		boolean b = userService.ajaxValidateLoginname(loginname);
		//3.发给客户端
		resp.getWriter().print(b);
		
		return null;
	}
	/**
	 * ajaxEmai是否注册校验
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public  String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		
		//1.得到用户名
		String email = req.getParameter("email");
		//2.通过service得到校验交过
		boolean b = userService.ajaxValidateEmail(email);
		//3.发给客户端
		resp.getWriter().print(b);
		return null;
	}
	/**
	 * ajax验证码是否正确校验
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public  String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		//1.获取文本框,输入框中的校验码
		String verifyCode =	req.getParameter("verifyCode");
		//2.获取图片上真实的验证码
		String vcode = (String)req.getSession().getAttribute("vCode");
		//3.进行比较（忽略大小写）
		boolean b = verifyCode.equalsIgnoreCase(vcode);
		//4.发给客户端
		resp.getWriter().print(b);
		return null;
	}
	
	
}
