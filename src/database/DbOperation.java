package database;
import java.sql.*;

public class DbOperation {
	private String Driver = "com.mysql.jdbc.Driver";
	private String URL = "jdbc:mysql://localhost:3306/pytest?useSSL=true";
	private String userName = "root";
	private String password = "www";
	private Connection conn;
	private PreparedStatement stmt;
	//连接池连接
	public DbOperation(){
		conn = DbConnPool.getConnection();
	}
/*	//传统方式连接
 	public DbOperation(){
		try{
			Class.forName(Driver);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		try{
			conn = DriverManager.getConnection(URL,userName,password);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}*/
	public void createPStatement(String sql){
		try {
			stmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ResultSet executeQuery(String[] param){
		ResultSet rs=null;
		try{
			for(int i=1;i<=param.length;i++)
				stmt.setString(i, param[i-1]);
			rs = stmt.executeQuery();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return rs;
	}
	public void executeUpdate(String[] param){
		try{
			for(int i=1;i<=param.length;i++)
				stmt.setString(i, param[i-1]);

			stmt.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public void close(){
		try{
			stmt.close();
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

}
