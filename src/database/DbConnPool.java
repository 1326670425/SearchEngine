package database;
import java.sql.*;
import javax.naming.*;
import javax.sql.DataSource;

public class DbConnPool {
	private static DataSource dataSource;
	private static Connection conn;
	public static Connection getConnection(){
		try{
			Context initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/pytest");
			try{
				conn = dataSource.getConnection();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}

}
