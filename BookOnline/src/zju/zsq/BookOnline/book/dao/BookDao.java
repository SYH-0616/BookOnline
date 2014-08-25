package zju.zsq.BookOnline.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import zju.zsq.BookOnline.book.domain.Book;
import zju.zsq.BookOnline.pager.Expression;
import zju.zsq.BookOnline.pager.PageBean;
import zju.zsq.BookOnline.pager.PageConstants;
import zju.zsq.jdbc.TxQueryRunner;


public class BookDao {
	
	private QueryRunner qr = new TxQueryRunner();
	
	
	
	public PageBean<Book> findByCategory(String cid, int pc) throws SQLException{
		List<Expression> list = new ArrayList<Expression>();
		list.add(new Expression("cid","",cid));
		return findByCriteria(list, pc);
	}
	/**
	 * ͨ�õĲ�ѯ����
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException 
	 */
	private PageBean<Book> findByCriteria(List<Expression> exprList , int pc ) throws SQLException{
		/**
		 * 1.�õ�ps
		 * 2.�õ�tr
		 * 3.�õ�beanlist
		 * 4.����pagebean
		 */
		int ps = PageConstants.BOOK_PAGE_SIZE;//ÿҳ��¼��
		//����where�Ӿ�
		StringBuilder whereSql = new StringBuilder(" where 1 =1 ");
		List<Object> params = new ArrayList<Object>();//��Ӧ�ʺŵ�ֵ
		for(Expression expr : exprList){
			whereSql.append(" and ").append(expr.getName()).append(" ").append(
					expr.getOperator()).append(" ");
			if(expr.getOperator().equals("is null")){
				whereSql.append("?");
				params.add(expr.getValue());//
			}
		}
		
		//�õ��ܼ�¼��
		String sql = "select count(*) from t_book"+whereSql;
		
		Number number = (Number)qr.query(sql, new ScalarHandler(),params.toArray());
		int tr = number.intValue();
		
		//�õ���ǰҳ��¼
		String sql1 = "select * from t_book "+whereSql + " order by orderBy limit ? , ?";
		
		params.add((pc-1)*ps);//��һ���ʺ� ��ǰ��¼���±�
		params.add(ps);//һ����ѯ������
		
		List<Book> beanList = qr.query(sql1, new BeanListHandler<Book>(Book.class),params.toArray());
		
		PageBean<Book> pb = new PageBean<Book>();
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		pb.setBeanList(beanList);
		
		return pb;
		
	}
}
