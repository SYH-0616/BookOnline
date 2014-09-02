package zju.zsq.BookOnline.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import zju.zsq.BookOnline.category.domain.Category;
import zju.zsq.commons.CommonUtils;
import zju.zsq.jdbc.TxQueryRunner;
/**
 *分类Dao
 * @author zhushiqing
 *
 */
public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();	 

	/**
	 * 把map转成一个category
	 * @param map
	 * @return
	 */
	private Category toCategory(Map<String,Object> map){
		//cid,cname,pid,desc,orderBy
		Category category = CommonUtils.toBean(map, Category.class);
		String pid = (String) map.get("pid");
		if(pid != null){//������ID��Ϊ�գ�����ʹ��һ��������װ��pid
			Category parent = new Category();
			parent.setCid(pid);
			category.setParent(parent);
		}
		return category;
	}
	
	private List<Category> toCategoryList(List<Map<String,Object>> mapList){
		List<Category> list = new ArrayList<Category>();
		for (Map<String,Object> map : mapList) {
			Category c = toCategory(map);
			list.add(c);
		}
		return list;
	}
	
	/**
	 * 查找所有分类
	 * @return
	 * @throws SQLException 
	 */
	public List<Category> findAll() throws SQLException{
		String sql = "select * from t_category where pid is null order by orderBy";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler());
		List<Category> parents = toCategoryList(mapList);
		
		for(Category  parent : parents){
			//
			List<Category> children = findByParent(parent.getCid());
			parent.setChildren(children);
		}
		
		return parents;
	}
	/**
	 * 根据父分类查找
	 * @throws SQLException 
	 */
	public List<Category> findByParent(String pid) throws SQLException{
		String sql = "select * from t_category where pid = ? order by orderBy";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),pid);
		return toCategoryList(mapList);
	}
	/**
	 * 
	 * @param category
	 * @throws SQLException 
	 */
	public void add(Category category) throws SQLException{
		String sql = "insert into t_category(cid,cname,pid,`desc`)values(?,?,?,?)";
		//因为一级分类没有pid而二级分类有，首先得判断
		String pid = null;
		if(category.getParent()!=null){
			pid = category.getParent().getCid();
		}
		Object[] params = {category.getCid(),category.getCname(),pid,category.getDesc()};
		qr.update(sql, params);
	}
	/**
	 * 返回所有父分类不带子分类
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findParents() throws SQLException{
		
		String sql = "select * from t_category where pid is null order by orderBy";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler()); 
		
		
		return toCategoryList(mapList);
	}
	
	public Category load(String cid) throws SQLException{
		String sql = "select * from t_category where cid = ?";
		return toCategory(qr.query(sql, new MapHandler(),cid));
	}
	/**
	 * 即可修改一级分类也可以修改二级分类
	 * @param category
	 * @throws SQLException 
	 */
	public void edit(Category category) throws SQLException{
		String sql = "update t_category set cname = ?,pid=?,`desc`=? where cid = ?";
		String pid = null;
		if(category.getParent()!=null){
			pid = category.getParent().getCid();
		}
		Object[] params = {category.getCname(),pid,
				category.getDesc(),category.getCid()};
		qr.update(sql, params);
	}
	
}
