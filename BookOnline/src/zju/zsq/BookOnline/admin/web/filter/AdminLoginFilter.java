package zju.zsq.BookOnline.admin.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class AdminLoginFilter
 */
@WebFilter(
		urlPatterns = { 
				"/AdminLoginFilter", 
				"/admin/*", 
				"/adminjsps/admin/*"
		}, 
		servletNames = { 
				"AdminAddBookServlet", 
				"AdminCategoryServlet", 
				"AdminOrderServlet", 
				"AdminServlet"
		})
public class AdminLoginFilter implements Filter {

    
    public AdminLoginFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest  req = (HttpServletRequest) request;
		Object admin = req.getSession().getAttribute("admin");
		if(admin==null){
			req.setAttribute("msg", "你还没登陆，不要乱干坏事！");
			req.getRequestDispatcher("/adminjsps/login.jsp").forward(req, response);
		}else{
			chain.doFilter(request, response);
		}
		
		
	}
	
	public void init(FilterConfig fConfig) throws ServletException { 
		
	}

}
