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
 * 用户web层开发
 * @author zhushiqing
 *
 */
public class UserServlet extends BaseServlet{
	private UserService userService = new UserService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
	}
	/**
	 * 用户名校验
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//获取用户名
		String loginname = req.getParameter("loginname");
		//通过service获取结果
		boolean b = userService.ajaxValidateLonginname(loginname);
		//发回给客户端
		resp.getWriter().print(b);
		return null;
	}
	/**
	 * Email校验
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//获取用户名
		String email = req.getParameter("email");
		//通过service获取结果
		boolean b = userService.ajaxValidateLonginname(email);
		//发回给客户端
		resp.getWriter().print(b);
		return null;
	}
	/**
	 * 验证码校验
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1.获取用户输入的验证码
		String verifyCode = req.getParameter("verifyCode");
		//2.获取图片中真实的验证码
		String vcode = (String)req.getSession().getAttribute("vCode");
		//3.做比较
		boolean b = verifyCode.equals(vcode);
		//4.返回客户端
		resp.getWriter().print(b);
		return null;
	}
	
	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 1.封装表单数据到User里面
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		/*
		 * 2.校验，如果校验失败，保存错误信息，返回regist.jsp显示
		 */
		Map<String,String> errors = validateRegist(formUser, req.getSession());
		if(errors.size()>0){
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/regist.jsp";
		}
		/*
		 * 3.使用service完成业务
		 */
		userService.regist(formUser);
		/*
		 * 4.保存成功信息，转发到msg.jsp显示！
		 */
		req.setAttribute("code", "success");
		req.setAttribute("msg", "恭喜您，注册成功，请马上去邮箱激活");
		
		return "f:/jsps/msg.jsp";
		//f开头代表转发，带了数据了
	}
	
	private Map<String,String> validateRegist(User userForm, HttpSession session){
		Map <String,String> errors = new HashMap<String,String>();
		//校验登录名
		String loginname = userForm.getLoginname();
		if(loginname.trim().isEmpty()||loginname==null){
			errors.put("loginname", "用户名不能为空");
		}else if(loginname.length()>20||loginname.length()<6){
			errors.put("loginname", "用户名的长度必须在6~20之间");
		}else if(!userService.ajaxValidateLonginname(loginname)){
			errors.put("loginname", "用户名已被注册");
		}
		
		//校验登录密码
		String loginpass = userForm.getLoginpass();
		if(loginpass.trim().isEmpty()||loginpass==null){
			errors.put("loginpass", "密码不能为空");
		}else if(loginpass.length()>20||loginpass.length()<6){
			errors.put("loginpass", "密码的长度必须在6~20之间");
		}
		//确认密码校验
		String reloginpass = userForm.getReloginpass();
		if(reloginpass.trim().isEmpty()||reloginpass==null){
			errors.put("reloginpass", "确认密码不能为空");
		}else if(!reloginpass.equals(loginpass)){
			errors.put("reloginpass", "两次输入不一致");
		}
		//email校验
		String email = userForm.getEmail();
		if(email.trim().isEmpty()||email==null){
			errors.put("email", "email不能为空！");
		}else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")){
			errors.put("email", "email格式错误！");
		}else if(!userService.ajaxValidateEmail(email)){
			errors.put("email", "email已经被注册！");
		}
		//验证码校验
		String verifyCode = userForm.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode.trim().isEmpty()||verifyCode==null){
			errors.put("verifyCode", "验证码不能为空！");
		}else if(!verifyCode.equalsIgnoreCase(vcode)){
			errors.put("verifyCode", "验证码错误！");
		}
	return errors;
	}
	
	public String activation(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		/*
		 * 1.获取参数激活码
		 * 2.用激活码调用service方法
		 * service方法可能抛出异常，把异常信息拿来封装到request中，转发到msg.jsp
		 * 3.保存信息到request，转发到msg.jsp
		 */
		
		String code = req.getParameter("activationCode");
		try {
			userService.activaction(code);
			req.setAttribute("code", "success");
			req.setAttribute("msg", "恭喜，激活成功");
		} catch (UserException e) {
			//说明service抛出异常
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("code", "error");//通知msg页面显示叉
		}
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * 登录校验
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException 
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException{
		/*
		 * 1. 封装表单数据到User
		 * 2. 校验表单数据
		 * 3. 使用service查询，得到User
		 * 4. 查看用户是否存在，如果不存在：
		 *   * 保存错误信息：用户名或密码错误
		 *   * 保存用户数据：为了回显
		 *   * 转发到login.jsp
		 * 5. 如果存在，查看状态，如果状态为false：
		 *   * 保存错误信息：您没有激活
		 *   * 保存表单数据：为了回显
		 *   * 转发到login.jsp
		 * 6. 登录成功：
		 * 　　* 保存当前查询出的user到session中
		 *   * 保存当前用户的名称到cookie中，注意中文需要编码处理。
		 */
		/*
		 * 1. 封装表单数据到user
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		/*
		 * 2. 校验
		 */
		Map<String,String> errors = validateLogin(formUser, req.getSession());
		if(errors.size() > 0) {
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/login.jsp";
		}
		
		/*
		 * 3. 调用userService#login()方法
		 */
		User user = userService.login(formUser);
		/*
		 * 4. 开始判断
		 */
		if(user == null) {
			req.setAttribute("msg", "用户名或密码错误！");
			req.setAttribute("user", formUser);
			return "f:/jsps/user/login.jsp";
		} else {
			if(!user.isStatus()) {
				req.setAttribute("msg", "您还没有激活！");
				req.setAttribute("user", formUser);
				return "f:/jsps/user/login.jsp";				
			} else {
				// 保存用户到session
				req.getSession().setAttribute("sessionUser", user);
				// 获取用户名保存到cookie中
				String loginname = user.getLoginname();
				loginname = URLEncoder.encode(loginname, "utf-8");
				Cookie cookie = new Cookie("loginname", loginname);
				cookie.setMaxAge(60 * 60 * 24 * 10);//保存10天
				resp.addCookie(cookie);
				return "r:/index.jsp";//重定向到主页
			}
		}
	}
	//登录的方法校验
	private Map<String,String> validateLogin(User userForm, HttpSession session) throws SQLException{
		Map <String,String> errors = new HashMap<String,String>();
		//校验登录名
		String loginname = userForm.getLoginname();
		String password = userForm.getLoginpass();
		if(loginname.trim().isEmpty()||loginname==null){
			errors.put("loginname", "用户名不能为空");
		}else if(password.trim().isEmpty()||password==null){
			errors.put("loginpass", "密码不能为空");
		}else if(userService.login(userForm)==null){
			errors.put("msg", "用户名或者密码错误");
		}
		String verifyCode = userForm.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode.trim().isEmpty()||verifyCode==null){
			errors.put("verifyCode", "验证码不能为空！");
		}else if(!verifyCode.equalsIgnoreCase(vcode)){
			errors.put("verifyCode", "验证码错误！");
		}
		
		
	return errors;
	}
}
