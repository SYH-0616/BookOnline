package zju.zsq.BookOnline.admin.category.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zju.zsq.BookOnline.category.domain.Category;
import zju.zsq.BookOnline.category.service.CategoryService;
import zju.zsq.commons.CommonUtils;
import zju.zsq.servlet.BaseServlet;


@WebServlet(name = "AdminCategoryServlet", urlPatterns = { "/admin/AdminCategoryServlet" })
public class AdminCategoryServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
	private CategoryService adminCategoryService = new CategoryService();
    

	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("parents", adminCategoryService.findAll());
		return "f:/adminjsps/admin/category/list.jsp";
	}
	//添加父分类
	public String addParent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Category parent = CommonUtils.toBean(request.getParameterMap(), Category.class);
		parent.setCid(CommonUtils.uuid());//设置cid
		adminCategoryService.add(parent);
		return findAll(request,response);
	}
	//
	public String addChildPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取父分类cid
		String pid = request.getParameter("pid");
		//获取所有父分类
		List<Category> parents =adminCategoryService.findParents();
		request.setAttribute("pid", pid);
		request.setAttribute("parents", parents);
		
		
		return	"f:/adminjsps/admin/category/add2.jsp";
	}
	//添加子分类
	public String addChild(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Category child = CommonUtils.toBean(request.getParameterMap(), Category.class);
		child.setCid(CommonUtils.uuid());//设置cid
	
		//手动映射到pid
		String pid = request.getParameter("pid");
		Category parent = new Category();
		parent.setCid(pid);
		child.setParent(parent);
		adminCategoryService.add(child);
		
		return findAll(request,response);
	}
	/**
	 * 修改一级分类第二步
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editParent(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Category parent = CommonUtils.toBean(request.getParameterMap(), Category.class);
		adminCategoryService.edit(parent);
		return findAll(request,response);
	}
	/**
	 * 修改一级分类第一步
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editParentPre(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		Category category = adminCategoryService.load(cid); 
		request.setAttribute("parent", category);
		return "f:/adminjsps/admin/category/edit.jsp";
	}
	/**
	 * 修改二级分类：第一步
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editChildPre(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		Category child = adminCategoryService.load(cid);
		request.setAttribute("child", child);
		request.setAttribute("parents", adminCategoryService.findParents());
		return "f:/adminjsps/admin/category/edit2.jsp";
	}
	/**
	 * 修改二级分类:第二步
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editChild(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Category child = CommonUtils.toBean(request.getParameterMap(), Category.class);
		String pid = request.getParameter("pid");
		Category parent = new Category();
		parent.setCid(pid);
		child.setParent(parent);
		
		adminCategoryService.edit(child);
		return findAll(request, response);
		
	}
}
