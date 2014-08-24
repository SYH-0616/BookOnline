package zju.zsq.BookOnline.category.service;

import java.sql.SQLException;
import java.util.List;

import zju.zsq.BookOnline.category.dao.CategoryDao;
import zju.zsq.BookOnline.category.domain.Category;

/**
 * 分类模块的业务层
 * @author zhushiqing
 *
 */
public class CategoryService {

	private CategoryDao categoryDao = new CategoryDao();
	
	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll(){
		try {
			return categoryDao.findAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
