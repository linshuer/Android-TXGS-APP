package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.Article;

import db.DataBaseConnection;

public class ArticleDAOImpl implements ArticleDAO{

	@Override
	public void insert(Article article) throws Exception {  
        String sql = "INSERT INTO article(title,type,picimage,master,mnickname,date,content) VALUES(?,?,?,?,?,?,?)" ;  
        PreparedStatement pstmt = null ;  
        DataBaseConnection dbc = null ;  
        // 下面是针对数据库的具体操作  
        try{  
            // 连接数据库  
            dbc = new DataBaseConnection() ;  
            pstmt = dbc.getConnection().prepareStatement(sql) ;  
            pstmt.setString(1, article.getTitle());  
            pstmt.setString(2, article.getType());
            pstmt.setString(3, article.getPicimage());  
            pstmt.setString(4, article.getMaster()); 
            pstmt.setString(5, article.getMnickname());
            pstmt.setString(6, article.getDate());  
            pstmt.setString(7, article.getContent()); 
            // 进行数据库更新操作  
            pstmt.executeUpdate() ;  
            pstmt.close() ;  
        }catch (Exception e){  
            throw new Exception("操作出现异常") ;  
        }  
        finally{  
            // 关闭数据库连接  
            dbc.close() ;  
        }  
    }

	@Override
	public void delete(int articleid) throws Exception {
		 String sql = "DELETE FROM article WHERE articleid=?" ;  
	        PreparedStatement pstmt = null ;  
	        DataBaseConnection dbc = null ;  
	        // 下面是针对数据库的具体操作  
	        try{  
	            // 连接数据库  
	            dbc = new DataBaseConnection() ;  
	            pstmt = dbc.getConnection().prepareStatement(sql) ;           
	            pstmt.setInt(1,articleid) ;  
	            // 进行数据库更新操作  
	            pstmt.executeUpdate() ;  
	            pstmt.close() ;  
	        }catch (Exception e){  
	            throw new Exception("操作出现异常") ;  
	        }  
	        finally{  
	            // 关闭数据库连接  
	            dbc.close() ;  
	        }  

	}

	@Override
	public Article queryById(int articleid) throws Exception {
		String sql = "SELECT * FROM article WHERE articleid=?" ;
		PreparedStatement pstmt = null;
		DataBaseConnection dbc = null;
		ResultSet rs=null;
		Article article = new Article();
		// 下面是针对数据库的具体操作
		try {
			dbc = new DataBaseConnection() ;  
            pstmt = dbc.getConnection().prepareStatement(sql) ;           
            pstmt.setInt(1, articleid); 
            // 进行数据库查询操作  
            rs= pstmt.executeQuery() ; 
			if (rs.next()) {
				// 查询出内容，之后将查询出的内容赋值给user对象
				article.setArticleid(rs.getInt("articleid"));
				article.setTitle(rs.getString("title"));
				article.setPicimage(rs.getString("picimage"));
				article.setMaster(rs.getString("master"));
				article.setMnickname(rs.getString("mnickname"));
				article.setType(rs.getString("type"));
				article.setDate(rs.getString("date"));
				article.setContent(rs.getString("content"));
				// 将查询出来的数据加入到List对象之中
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rs.close();
			pstmt.close();
			dbc.close();
		}
		return article;
	}

	@Override
	public List<Article>  queryAll(String master) throws Exception {
		List<Article> all = null;
		String sql = "SELECT * FROM article WHERE master=?" ;
		PreparedStatement pstmt = null;
		DataBaseConnection dbc = null;
		ResultSet rs=null;
		// 下面是针对数据库的具体操作
		try {
			dbc = new DataBaseConnection() ;  
            pstmt = dbc.getConnection().prepareStatement(sql) ;           
            pstmt.setString(1, master);  
            // 进行数据库查询操作  
            rs= pstmt.executeQuery() ; 
			all =new ArrayList<Article>();
			while (rs.next()) {
				// 查询出内容，之后将查询出的内容赋值给user对象
				Article article = new Article();
				article.setArticleid(rs.getInt("articleid"));
				article.setTitle(rs.getString("title"));
				article.setPicimage(rs.getString("picimage"));
				article.setMaster(rs.getString("master"));
				article.setMnickname(rs.getString("mnickname"));
				article.setType(rs.getString("type"));
				article.setDate(rs.getString("date"));
				article.setContent(rs.getString("content"));
				// 将查询出来的数据加入到List对象之中
				all.add(article);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rs.close();
			pstmt.close();
			dbc.close();
		}
		return all;
	}

}
