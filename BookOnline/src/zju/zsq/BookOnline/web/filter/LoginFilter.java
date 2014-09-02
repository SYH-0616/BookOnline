package zju.zsq.BookOnline.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import zju.zsq.BookOnline.user.domain.User;


public class LoginFilter implements Filter {


	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		//1.获取session的user
		HttpServletRequest req = (HttpServletRequest) request;
		User user = (User)req.getSession().getAttribute("SessionUser");
		if(user==null){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "您还没有登录，不能访问本资源！");
			req.getRequestDispatcher("/jsps/msg.jsp").forward(req, response);
		}else{
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException { 	
		 
	}

}
