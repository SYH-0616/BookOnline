package zju.zsq.BookOnline.category.service;

import java.sql.SQLException;
import java.util.List;

import zju.zsq.BookOnline.category.dao.CategoryDao;
import zju.zsq.BookOnline.category.domain.Category;

/**
 * 
 * @author zhushiqing
 *
 */
public class CategoryService {

	private CategoryDao categoryDao = new CategoryDao();
	
	/**
	 * 加载分类
	 * @param cid
	 * @return
	 */
	public Category load(String cid){
		try {
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void edit(Category category){
		try {
			categoryDao.edit(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查看所有分类
	 * @return
	 */
	public List<Category> findAll(){
		try {
			return categoryDao.findAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 添加分类
	 * @param category
	 */
	public void add(Category category){
		try {
			categoryDao.add(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}		
	}
	/**
	 * 获取所有父分类，不带子分类
	 * @return
	 */
	public List<Category> findParents(){
		try {
			return categoryDao.findParents();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 查询指定分类的子分类的个数
	 * @param pid
	 * @return
	 */
	public int findChildrenCountByParent(String pid){
		try {
			return categoryDao.findChildrenCountByParents(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 删除
	 * @param cid
	 */
	public void delete(String cid){
		try {
			categoryDao.delete(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 查询指定父分类下的子分类
	 * @param pid
	 * @return
	 */
	public List<Category> findByParent(String pid){
		try {
			return categoryDao.findByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
