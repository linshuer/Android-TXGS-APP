package dao;

import java.sql.ResultSet;
import java.util.List;

import bean.Article;
import bean.User;

public interface ArticleDAO {
	//插入
	 public void insert(Article article) throws Exception ;  
	 // 删除操作  
	 public void delete(int articleid) throws Exception ; 
	 // 按ID查询操作  
	 public Article queryById(int articleid) throws Exception ;  
	 // 查询全部  
	 public List queryAll(String master) throws Exception ;
}
