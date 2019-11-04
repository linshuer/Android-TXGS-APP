package dao;

public class DAOFactory{  
    public static UserDAO getUserDAOInstance(){  
        return new UserDAOImpl() ;  
    }  
    public static ArticleDAOImpl getArticleDAOInstance(){  
        return new ArticleDAOImpl() ;  
    } 
} 