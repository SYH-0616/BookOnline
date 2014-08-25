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
	 * 通用的查询方法
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException 
	 */
	private PageBean<Book> findByCriteria(List<Expression> exprList , int pc ) throws SQLException{
		/**
		 * 1.得到ps
		 * 2.得到tr
		 * 3.得到beanlist
		 * 4.创建pagebean
		 */
		int ps = PageConstants.BOOK_PAGE_SIZE;//每页记录数
		//生成where子句
		StringBuilder whereSql = new StringBuilder(" where 1 =1 ");
		List<Object> params = new ArrayList<Object>();//对应问号的值
		for(Expression expr : exprList){
			whereSql.append(" and ").append(expr.getName()).append(" ").append(
					expr.getOperator()).append(" ");
			if(expr.getOperator().equals("is null")){
				whereSql.append("?");
				params.add(expr.getValue());//
			}
		}
		
		//得到总记录数
		String sql = "select count(*) from t_book"+whereSql;
		
		Number number = (Number)qr.query(sql, new ScalarHandler(),params.toArray());
		int tr = number.intValue();
		
		//得到当前页记录
		String sql1 = "select * from t_book "+whereSql + " order by orderBy limit ? , ?";
		
		params.add((pc-1)*ps);//第一个问号 当前记录的下标
		params.add(ps);//一共查询多少行
		
		List<Book> beanList = qr.query(sql1, new BeanListHandler<Book>(Book.class),params.toArray());
		
		PageBean<Book> pb = new PageBean<Book>();
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		pb.setBeanList(beanList);
		
		return pb;
		
	}
}
