package zju.zsq.BookOnline.category.service;

import java.sql.SQLException;
import java.util.List;

import zju.zsq.BookOnline.category.dao.CategoryDao;
import zju.zsq.BookOnline.category.domain.Category;

/**
 * ����ģ���ҵ���
 * @author zhushiqing
 *
 */
public class CategoryService {

	private CategoryDao categoryDao = new CategoryDao();
	
	/**
	 * ��ѯ���з���
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
