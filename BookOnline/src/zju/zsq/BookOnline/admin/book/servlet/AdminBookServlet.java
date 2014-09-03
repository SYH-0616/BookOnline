package zju.zsq.BookOnline.admin.book.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zju.zsq.BookOnline.book.domain.Book;
import zju.zsq.BookOnline.book.service.BookService;
import zju.zsq.BookOnline.category.domain.Category;
import zju.zsq.BookOnline.category.service.CategoryService;
import zju.zsq.BookOnline.pager.PageBean;
import zju.zsq.commons.CommonUtils;
import zju.zsq.servlet.BaseServlet;

@WebServlet("/admin/AdminBookServlet")
public class AdminBookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();

	/**
	 * 显示所有的分类
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Category> list = categoryService.findAll();
		req.setAttribute("parents", list);
		return "f:/adminjsps/admin/book/left.jsp";
	}

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
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 1.得到pc，如果页面传递，则使用页面的，如果页面没有传递，pc=1
		// 2.得到url
		// 3.获取查询条件，获取cid
		// 4.使用pc与cid 调用findByCategory得到pageBean
		// 5.给PageBean
		int pc = getPc(request);
		String url = getUrl(request);
		String cid = request.getParameter("cid");
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	public String findByBname(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 1.得到pc，如果页面传递，则使用页面的，如果页面没有传递，pc=1
		// 2.得到url
		// 3.获取查询条件，获取cid
		// 4.使用pc与cid 调用findByCategory得到pageBean
		// 5.给PageBean
		int pc = getPc(request);
		String url = getUrl(request);
		String bname = request.getParameter("bname");
		PageBean<Book> pb = bookService.findByBname(bname, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f://adminjsps/admin/book/list.jsp";
	}

	public String findByAuthor(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 1.得到pc，如果页面传递，则使用页面的，如果页面没有传递，pc=1
		// 2.得到url
		// 3.获取查询条件，获取cid
		// 4.使用pc与cid 调用findByCategory得到pageBean
		// 5.给PageBean
		int pc = getPc(request);
		String url = getUrl(request);
		String author = request.getParameter("author");
		PageBean<Book> pb = bookService.findByAuthor(author, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	public String findByPress(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 1.得到pc，如果页面传递，则使用页面的，如果页面没有传递，pc=1
		// 2.得到url
		// 3.获取查询条件，获取cid
		// 4.使用pc与cid 调用findByCategory得到pageBean
		// 5.给PageBean
		int pc = getPc(request);
		String url = getUrl(request);
		String press = request.getParameter("press");
		PageBean<Book> pb = bookService.findByPress(press, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	public String findByCombination(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 1.得到pc，如果页面传递，则使用页面的，如果页面没有传递，pc=1
		// 2.得到url
		// 3.获取查询条件，获取cid
		// 4.使用pc与cid 调用findByCategory得到pageBean
		// 5.给PageBean
		int pc = getPc(request);
		String url = getUrl(request);
		Book criteria = CommonUtils.toBean(request.getParameterMap(),
				Book.class);
		PageBean<Book> pb = bookService.findByCombination(criteria, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * 添加图书第一步
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addPre(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Category> parents = categoryService.findAll();
		request.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/add.jsp";
	}
	//{"cid":"asdasdsad","cname":"asdasda"}
	private String toJson(Category category) {
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
		sb.append(",");
		sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}

	
	//[{"cid":"asdasdsad","cname":"asdasda"},{"cid":"asdasdsad","cname":"asdasda"}]
	private String toJson(List<Category> categoryList) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < categoryList.size(); i++) {
			sb.append(toJson(categoryList.get(i)));
			if(i<categoryList.size()-1){
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public String ajaxFindChildren(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 1.获取pid
		String pid = request.getParameter("pid");
		// 2.通过pid查询所有二级分类
		List<Category> children = categoryService.findByParent(pid);
		// 3.把children变成json
		String json = toJson(children);
		System.out.println(json);
		// 4.输出
		response.getWriter().print(json);
		return null;
	}
	/**
	 * 加载图书
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String bid = request.getParameter("bid");
		Book book = bookService.load(bid);
		request.setAttribute("book", book);
		
		request.setAttribute("parents", categoryService.findParents());
		
		String pid = book.getCategory().getParent().getCid();
		request.setAttribute("children", categoryService.findByParent(pid));
		
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	
	public String edit(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Map map = request.getParameterMap();
		Book book = CommonUtils.toBean(map, Book.class);
		Category category = CommonUtils.toBean(map, Category.class);
		
		book.setCategory(category);
		
		bookService.edit(book);
		request.setAttribute("msg", "修改图书成功！");
		return "f:/adminjsps/msg.jsp";
			 
	}
	
	public String deletet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String bid = request.getParameter("bid");
		
		Book book = bookService.load(bid);
		String path = this.getServletContext().getRealPath("/");//获取真实路径
		new File(path+book.getImage_b()).delete();//删除文件
		new File(path+book.getImage_w()).delete();
		
		bookService.delete(bid);//删除数据库的记录
		
		request.setAttribute("msg", "删除图书成功！");
		return "f:/adminjsp/msg.jsp";
	}
}
