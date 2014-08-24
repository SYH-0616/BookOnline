package zju.zsq.BookOnline.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import zju.zsq.BookOnline.category.domain.Category;
import zju.zsq.commons.CommonUtils;
import zju.zsq.jdbc.TxQueryRunner;
/**
 * ����ģ��־ò�
 * @author zhushiqing
 *
 */
public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();	 

	/**
	 * ��һ��map�е�����ӳ�䵽Category��
	 * @param map
	 * @return
	 */
	private Category toCategory(Map<String,Object> map){
		//cid,cname,pid,desc,orderBy
		Category category = CommonUtils.toBean(map, Category.class);
		String pid = (String) map.get("pid");
		if(pid != null){//���������ID��Ϊ�գ�����ʹ��һ��������װ��pid
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
	 * ��������һ������
	 * @return
	 * @throws SQLException 
	 */
	public List<Category> findAll() throws SQLException{
		/**
		 * 1.��ѯ������һ������
		 */
		String sql = "select * from t_category where pid is null";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler());
		List<Category> parents = toCategoryList(mapList);
		
		for(Category  parent : parents){
			//���ҵ�ǰ������������ӷ���
			List<Category> children = findByParent(parent.getCid());
			parent.setChildren(children);
		}
		
		return parents;
	}
	/**
	 * ͨ���������ѯ�ӷ���
	 * @throws SQLException 
	 */
	public List<Category> findByParent(String pid) throws SQLException{
		String sql = "select * from t_category where pid = ?";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),pid);
		return toCategoryList(mapList);
	}
}
