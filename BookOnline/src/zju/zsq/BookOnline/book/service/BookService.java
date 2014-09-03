package zju.zsq.BookOnline.book.service;

import java.sql.SQLException;

import zju.zsq.BookOnline.book.dao.BookDao;
import zju.zsq.BookOnline.book.domain.Book;
import zju.zsq.BookOnline.pager.PageBean;

public class BookService {
	private BookDao  bookDao = new BookDao();
	
	/**
	 * 加载图书
	 * @param bid
	 * @return
	 */
	public Book load(String bid) {
		try {
			return bookDao.findByBid(bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//按分类查
	public PageBean<Book> findByCategory(String cid, int pc){
		try {
			return bookDao.findByCategory(cid, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//按照书名查询
	public PageBean<Book> findByBname(String bname, int pc){
		try {
			return bookDao.findByBname(bname, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//按照作者查询
	public PageBean<Book> findByAuthor(String author, int pc){
		try {
			return bookDao.findByAuthor(author, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//按照出本社查询
	public PageBean<Book> findByPress(String press, int pc){
		try {
			return bookDao.findByPress(press, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//多条件组合查询
	public PageBean<Book> findByCombination(Book combination, int pc){
		try {
			return bookDao.findByCombination(combination, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 返回当前分类下图书的个数
	 * @param cid
	 * @return
	 */
	public int findBookCountByCategory(String cid){
		try {
			return bookDao.findBookCountCategory(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void add(Book book){
		try{
			bookDao.add(book);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	public void edit(Book book){
		try{
			bookDao.edit(book);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	public void delete(String bid){
		try{
			bookDao.delete(bid);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
}
