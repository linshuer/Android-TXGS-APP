package dao;

import java.util.* ;

import bean.*
;//定义数据库操作方法  
public interface UserDAO{  
 // 增加操作  
 public void insert(User user) throws Exception ;  
 // 修改操作  
 public void update(User user) throws Exception ;  
 //根据账号名获得User
 public User getNowUser(String username) throws Exception; 
 //判断用户名是否存在
 public boolean isUser(String username) throws Exception;
 //判断用户及密码是否正确
 public boolean isUserAndPassword(String username,String password) throws Exception;
 //pssword
 public void alterpassword(User user) throws Exception;

 
 //以下暂时不需要
 // 删除操作  
/* public void delete(int userid) throws Exception ; */ 
 // 按ID查询操作  
 public User queryById(int userid) throws Exception ;  
 // 查询全部  
 public List queryAll() throws Exception ;
 
}  