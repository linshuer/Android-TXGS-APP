package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import bean.User;
import db.DataBaseConnection;

public class UserDAOImpl implements UserDAO {

	@Override
	public void insert(User user) throws Exception {  
        String sql = "INSERT INTO user(username,password,headimage,nickname,sex,address,autograph,theme) VALUES(?,?,?,?,?,?,?,?)" ;  
        PreparedStatement pstmt = null ;  
        DataBaseConnection dbc = null ;  
        // 下面是针对数据库的具体操作  
        try{  
            // 连接数据库  
            dbc = new DataBaseConnection() ;  
            pstmt = dbc.getConnection().prepareStatement(sql) ;  
            pstmt.setString(1, user.getUsername());  
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getHeadimage());  
            pstmt.setString(4, user.getNickname()); 
            pstmt.setString(5, user.getSex());  
            pstmt.setString(6, user.getAddress()); 
            pstmt.setString(7, user.getAutopraph());
            pstmt.setString(8, user.getTheme()); 
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
	public void update(User user) throws Exception {  
        String sql = "UPDATE user SET password=?,headimage=?,nickname=?,sex=?,address=?,autograph=?,theme=? WHERE username=?" ;  
        PreparedStatement pstmt = null ;  
        DataBaseConnection dbc = null ;  
        // 下面是针对数据库的具体操作  
        try{  
            // 连接数据库  
            dbc = new DataBaseConnection() ;  
            pstmt = dbc.getConnection().prepareStatement(sql) ;  
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getHeadimage());  
            pstmt.setString(3, user.getNickname()); 
            pstmt.setString(4, user.getSex());  
            pstmt.setString(5, user.getAddress()); 
            pstmt.setString(6, user.getAutopraph());
            pstmt.setString(7, user.getTheme()); 
            pstmt.setString(8, user.getUsername());  
            // 进行数据库更新操作  
            pstmt.executeUpdate() ;  
            pstmt.close() ;  
        }  
        catch (Exception e){  
            throw new Exception("操作出现异常") ;  
        }  
        finally{  
            // 关闭数据库连接  
            dbc.close() ;  
        }  
    }

	@Override
	public User getNowUser(String username) throws Exception {
		User user = null;  
        String sql = "SELECT * FROM user WHERE username=?" ;  
        PreparedStatement pstmt = null ;  
        DataBaseConnection dbc = null ;  
        // 下面是针对数据库的具体操作  
        try{  
            // 连接数据库  
            dbc = new DataBaseConnection() ;  
            pstmt = dbc.getConnection().prepareStatement(sql) ;           
            pstmt.setString(1, username);  
            // 进行数据库查询操作  
            ResultSet rs = pstmt.executeQuery() ;  
            if(rs.next())  
            {  
                // 查询出内容，之后将查询出的内容赋值给user对象  
                user = new User() ;
                user.setUsername(rs.getString("username"));  
                user.setPassword(rs.getString("password"));
                user.setHeadimage(rs.getString("headimage"));
                user.setNickname(rs.getString("nickname"));
                user.setSex(rs.getString("sex"));
                user.setAddress(rs.getString("address"));
                user.setAutopraph(rs.getString("autograph"));
                user.setTheme(rs.getString("theme"));
            }  
            rs.close() ;  
            pstmt.close() ;  
        }catch (Exception e){  
            throw new Exception("操作出现异常") ;  
        }  
        finally{  
            // 关闭数据库连接  
            dbc.close() ;  
        }  
        return user ;  
	}

	@Override
	public boolean isUser(String username) throws Exception {
		boolean isuser =false;
        String sql = "SELECT * FROM user WHERE username=?" ;  
        PreparedStatement pstmt = null ;  
        DataBaseConnection dbc = null ;  
        // 下面是针对数据库的具体操作  
        try{  
            // 连接数据库  
            dbc = new DataBaseConnection() ;  
            pstmt = dbc.getConnection().prepareStatement(sql) ;           
            pstmt.setString(1, username);  
            // 进行数据库查询操作  
            ResultSet rs = pstmt.executeQuery() ;  
            if(rs.next()){
                isuser = true;
            }else{
            	isuser=false;
            }
            rs.close();  
            pstmt.close();
        }catch (Exception e){  
            throw new Exception("操作出现异常") ;  
        }  
        finally{  

            dbc.close() ;  
        }
        return isuser;
	}

	@Override
	public boolean isUserAndPassword(String username, String password) throws Exception {
		boolean isuser =false;
        String sql = "SELECT * FROM user WHERE username=? AND password=?" ;  
        PreparedStatement pstmt = null ;  
        DataBaseConnection dbc = null ;  
        // 下面是针对数据库的具体操作  
        try{  
            // 连接数据库  
            dbc = new DataBaseConnection() ;  
            pstmt = dbc.getConnection().prepareStatement(sql) ;           
            pstmt.setString(1, username);
            pstmt.setString(2, password); 
            // 进行数据库查询操作  
            ResultSet rs = pstmt.executeQuery() ;  
            if(rs.next()){
                isuser = true;
            }else{
            	isuser=false;
            }
            rs.close();  
            pstmt.close();
        }catch (Exception e){  
            throw new Exception("操作出现异常") ;  
        }  
        finally{  

            dbc.close() ;  
        }
        return isuser;
	}

	@Override
	public void alterpassword(User user) throws Exception {
		// TODO Auto-generated method stub

	}

/*	@Override
	public void delete(int userid) throws Exception {
		// TODO Auto-generated method stub

	}*/

	@Override
	public User queryById(int userid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List queryAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
