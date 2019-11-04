package split;

import java.sql.*;
import java.util.*;
import db.*;
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.util.ArrayList;  
import java.util.List;

import bean.*;
import dao.*;
public class SplitPage
{
	//定义数据库连接对象和结果集对象
	 
	private DataBaseConnection dbc = null ;
	private PreparedStatement pstmt = null ; 
	private ResultSet rs=null;
	//SQL查询语句
	private String sql;
	//总记录数目
	private int rowCount=0;
	//所分的逻辑页数
	private int pageCount=0;
	//每页显示的记录数目
	private int pageSize=0;	
	//设置参数值
	
	//初始化,获取数据表中的信息
	public List getPage(String sql,int pageSize,int ipage)
	{		
		int irows = pageSize*(ipage-1);
		this.sql=sql;
		this.pageSize=pageSize;
		List all = new ArrayList();
        // 下面是针对数据库的具体操作  
        try{  
            // 连接数据库  
            dbc = new DataBaseConnection() ;  
            pstmt = dbc.getConnection().prepareStatement(this.sql) ;
            rs=pstmt.executeQuery();
            // 进行数据库查询操作  
            if (rs!=null)
			{
				rs.last();
				this.rowCount = rs.getRow();
				rs.first();
				this.pageCount = (this.rowCount - 1) / this.pageSize + 1; 
			} 
			this.sql=sql+" limit "+irows+","+pageSize; 
			pstmt = dbc.getConnection().prepareStatement(this.sql) ;
            this.rs=pstmt.executeQuery();//==========================================
            //=====================================================
            
                while(rs.next()){  
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
            	

            rs.close() ;  
            pstmt.close() ;  
        }  
        catch (Exception e){  
        	e.printStackTrace();  
        }  
        finally{  
            // 关闭数据库连接  
            dbc.close() ;  
        }
		return all; 
		
	}	
	
	//获得页面总数
	public int getPageCount()
	{
		return this.pageCount;
	}	
	//获取数据表中记录总数
	public int getRowCount()
	{
		return this.rowCount;
	}
}

