package db;

import java.sql.*;

public class DataBaseConnection {
	// 配置数据库
	private final String DBDRIVER = "com.mysql.jdbc.Driver";
	private final String DBURL = "jdbc:mysql://localhost:3306/appdb";
	private final String DBUSER = "root";
	private final String DBPASSWORD = "root";
	private Connection conn = null;

	public DataBaseConnection() { // 数据库基类创建连接
		try {
			Class.forName(DBDRIVER);
			this.conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
		} catch (Exception e) {
			System.out.println("加载驱动失败");
		}
	}

	// 得到连接
	public Connection getConnection() {
		return conn;
	}

	// 关闭连接，方便使用
	public void close() {
		try {
			conn.close();
		} catch (Exception e) {
			System.out.println("数据库连接关闭失败");
		}
	}

}