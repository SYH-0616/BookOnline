package zju.zsq.BookOnline.category.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zju.zsq.BookOnline.category.domain.Category;
import zju.zsq.BookOnline.category.service.CategoryService;
import zju.zsq.servlet.BaseServlet;
/**
 * 用户分类web层
 * @author zhushiqing
 *
 */
@SuppressWarnings("serial")
public class CategoryServlet extends BaseServlet{

	private CategoryService categoryService = new CategoryService();
	
	/**
	 * 查询所有分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public  String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Category> list = categoryService.findAll();
		req.setAttribute("parents", list);
		return "f:/jsps/left.jsp";
	} 
	
	
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
}
