package zju.zsq.BookOnline.user.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import zju.zsq.BookOnline.user.domain.User;
import zju.zsq.BookOnline.user.service.UserService;
import zju.zsq.BookOnline.user.service.exception.UserException;
import zju.zsq.commons.CommonUtils;
import zju.zsq.servlet.BaseServlet;


/**
 * 用户模块WEB层
 * @author zhushiqing
 *
 */
public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();
	
	
	public String quit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
				
		req.getSession().invalidate();
		return "r:/jsps/user/login.jsp";
		
	}
	
	
	public String updatePassword(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		/**
		 * 1.封装表单数据到user中
		 * 2.从session中获取uid
		 * 3.使用uid和表单中的oldPass跟newPass
		 * 		3.1如果出现异常，保存异常信息到request中，转发到pwd.jsp
		 * 4.保存成功信息到request中
		 * 5.转发到msg.jsp中
		 */
		
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		User user = (User)req.getSession().getAttribute("SessionUser");
		if(user == null){
			req.setAttribute("msg", "您还没登陆！");
			return "f:/jsps/user/login.jsp";
		}
		try {
			userService.updatePassword(user.getUid(), formUser.getLoginpass() ,formUser.getNewpass());
			req.setAttribute("msg", "修改密码成功！");
			req.setAttribute("code", "success");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			req.setAttribute("msg" ,e.getMessage() );
			req.setAttribute("user", formUser);
			return "f:/jsps/user/login.jsp";
		}
	}
	
	
	/**
	 * 登陆功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		/**
		 * 1.封装表单数据到User
		 * 2.检验表单数据
		 * 3.使用service查询
		 * 4.查看用户是否存在，如果不存在
		 * 		4.1保存错误信息
		 * 		4.2保存用户信息，为了回显
		 * 		4.3转发到login.jsp
		 * 5.如果存在.查看状态如果为false
		 * 		5.1保存错误信息，您没有激活
		 * 		5.2保存表单信息，为了回显
		 * 		5.3转发到login.jsp
		 * 6.登录成功:
		 * 		6.1保存当前查询出的user到session
		 * 		6.2保存用户名称到cookie中，主要中文编码处理
		 */	
		//1
		User userForm= CommonUtils.toBean(req.getParameterMap(), User.class);
		//2
		Map<String,String> errors = validateLogin(userForm,req.getSession());
		if(errors.size() > 0){
			req.setAttribute("form", userForm);//回显
			req.setAttribute("errors", errors);
			return "f:/jsps/user/login.jsp";
		}
		//3
		User user = userService.Login(userForm);
		//4
		if(user==null){
			req.setAttribute("msg", "用户名或者密码错误！");
			req.setAttribute("user", userForm);
			return "f:/jsps/user/login.jsp";
		}else{
			if(!user.isStatus()){
				req.setAttribute("msg", "您还没激活！");
				req.setAttribute("SessionUser", user);
				return "f:/jsps/user/login.jsp";
			}else{
				req.getSession().setAttribute("SessionUser", user);
				String loginname = user.getLoginname();
				loginname = URLEncoder.encode(loginname,"UTF-8");
				Cookie cookie = new Cookie("loginname",loginname);
				cookie.setMaxAge(60*60*24*7);
				resp.addCookie(cookie);
				return "r:/index.jsp";
			}
		}
			
		
	}
	
	private Map<String,String> validateLogin(User userForm, HttpSession session){
		Map <String , String > errors = new HashMap<String, String>();

		
		return errors;
	}
	
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
		//1.封装表单数据到User对象
		User userForm = CommonUtils.toBean(req.getParameterMap(), User.class);	
		//2.校验（如果校验失败，保存错误信息返回regist.jsp显示）
		Map<String,String> errors = validateRegist(userForm,req.getSession());
		if(errors.size() > 0){
			req.setAttribute("form", userForm);//回显
			req.setAttribute("errors", errors);
			return "f:/jsps/user/regist.jsp";
		}
		//3.使用service完成业务
		userService.regist(userForm);
		//4.保存成功信息，转发到msg.jsp显示
		req.setAttribute("code", "success");
		req.setAttribute("msg", "注册成功，请到邮箱激活");
		return "f:/jsps/msg.jsp";
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
	
	public  String activation(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		/**
		 * 1.获取参数激活码
		 * 2.用激活码service方法完成激活
		 * 如果service方法有异常，把异常信息拿来，存在request，转发到msg.jsp
		 * 3.保存成功信息到request中，转发到msg.jsp中
		 */
		String code = req.getParameter("activationCode");
		try {
			userService.activation(code);
			req.setAttribute("code", "success");
			req.setAttribute("msg", "激活成功，您可以正常登陆了！");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (UserException e) {
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("code", "error");
		}
		
		return "f:/jsps/msg.jsp";
		
	}
	
	/**
	 * 对表单数据逐个校验，如有错误，则使用当前字段为key，错误信息
	 * @param userForm
	 * @param session
	 * @return
	 */
	private Map<String,String> validateRegist(User userForm, HttpSession session){
		Map <String , String > errors = new HashMap<String, String>();
		//1.校验登录名
		String loginname = userForm.getLoginname();
		if(loginname.trim().isEmpty()){
			errors.put("loginname", "用户名不能为空！");
		}else if(loginname.length() > 20 && loginname.length() < 6){
			errors.put("loginname", "用户名长度在6~20之间！");
		}else if(!userService.ajaxValidateLoginname(loginname)){
			errors.put("loginname", "用户名已被注册");
		}
		//2.校验登录密码
		String loginpass = userForm.getLoginpass();
		if(loginpass.trim().isEmpty()){
			errors.put("loginpass", "密码不能为空！");
		}else if(loginpass.length() > 20 && loginname.length() < 6){
			errors.put("loginpass", "密码长度在6~20之间！");
		}
		//3.确认密码校验
		String reloginpass = userForm.getReloginpass();
		if(reloginpass.trim().isEmpty()){
			errors.put("reloginpass", "密码不能为空！");
		}else if(!reloginpass.equals(loginpass)){
			errors.put("reloginpass", "两次输入不一致！");
		}
		//4.Email校验
		String email = userForm.getEmail();
		if(loginname.trim().isEmpty()){
			errors.put("email", "邮箱不能为空！");
		}else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")){
			errors.put("email", "邮箱格式错误！");
		}else if(!userService.ajaxValidateEmail(email)){
			errors.put("email", "邮箱已被注册");
		}
		//5.验证码校验
		String verifyCode = userForm.getVerifyCode();
		String vcode = (String)session.getAttribute("vCode");
		if(verifyCode==null||verifyCode.trim().isEmpty()){
			errors.put("verifyCode", "验证码不能为空！");
		}else if(!vcode.equalsIgnoreCase(verifyCode)){
			errors.put("verifyCode", "验证码错误！");
		}
		
		return errors;
	}
}
