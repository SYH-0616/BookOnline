package zju.zsq.BookOnline.book.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zju.zsq.BookOnline.book.domain.Book;
import zju.zsq.BookOnline.book.service.BookService;
import zju.zsq.BookOnline.pager.PageBean;
import zju.zsq.commons.CommonUtils;
import zju.zsq.servlet.BaseServlet;






public class BookServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
	
	private BookService bookService = new BookService();

	
	//获取当前页码
	private int getPc(HttpServletRequest request){
		int pc = 1;
		String param = request.getParameter("pc");
		if(param != null && !param.trim().isEmpty()){
			try{
				pc  = Integer.parseInt(param);
			}catch(RuntimeException e){
				
			}
		}
		return pc;
	}
	
	//截取URL，页面中的分页导航需要使用他作为超链接的目标
	private String getUrl(HttpServletRequest request){
		String url = request.getRequestURI()+ "?" +request.getQueryString();
		//
		int index = url.indexOf("&pc=");
		if(index != -1){
			url = url.substring(0, index);
		}
		return url;
	}
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");//获取链接的参数bid
		Book book = bookService.load(bid);//通过bid得到book对象
		req.setAttribute("book", book);//保存到req中
		return "f:/jsps/book/desc.jsp";//转发到desc.jsp
	}
	
	/**
	 * 按分类查询
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.得到pc，如果页面传递，则使用页面的，如果页面没有传递，pc=1
		//2.得到url
		//3.获取查询条件，获取cid
		//4.使用pc与cid 调用findByCategory得到pageBean
		//5.给PageBean
		int pc = getPc(request);
		String url = getUrl(request);
		String cid = request.getParameter("cid");
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	public String findByBname(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.得到pc，如果页面传递，则使用页面的，如果页面没有传递，pc=1
		//2.得到url
		//3.获取查询条件，获取cid
		//4.使用pc与cid 调用findByCategory得到pageBean
		//5.给PageBean
		int pc = getPc(request);
		String url = getUrl(request);
		String bname = request.getParameter("bname");
		PageBean<Book> pb = bookService.findByBname(bname, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	public String findByAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.得到pc，如果页面传递，则使用页面的，如果页面没有传递，pc=1
		//2.得到url
		//3.获取查询条件，获取cid
		//4.使用pc与cid 调用findByCategory得到pageBean
		//5.给PageBean
		int pc = getPc(request);
		String url = getUrl(request);
		String author = request.getParameter("author");
		PageBean<Book> pb = bookService.findByAuthor(author, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	public String findByPress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.得到pc，如果页面传递，则使用页面的，如果页面没有传递，pc=1
		//2.得到url
		//3.获取查询条件，获取cid
		//4.使用pc与cid 调用findByCategory得到pageBean
		//5.给PageBean
		int pc = getPc(request);
		String url = getUrl(request);
		String press = request.getParameter("press");
		PageBean<Book> pb = bookService.findByPress(press, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	public String findByCombination(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.得到pc，如果页面传递，则使用页面的，如果页面没有传递，pc=1
		//2.得到url
		//3.获取查询条件，获取cid
		//4.使用pc与cid 调用findByCategory得到pageBean
		//5.给PageBean
		int pc = getPc(request);
		String url = getUrl(request);
		Book criteria = CommonUtils.toBean(request.getParameterMap(), Book.class);
		PageBean<Book> pb = bookService.findByCombination(criteria, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
}
