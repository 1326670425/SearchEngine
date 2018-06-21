package database;
import java.sql.*;

/**
 * @ClassName DbOperation
 * @Description 执行数据库操作
 * @author 吴扬颉
 * @date 2018年6月4日
 * 
 */
public class DbOperation {

	private Connection conn;
	private PreparedStatement stmt;
	//连接池连接
	public DbOperation(){
		conn = DbConnPool.getConnection();
	}
/*	//传统方式连接
	private String Driver = "com.mysql.jdbc.Driver";
	private String URL = "jdbc:mysql://localhost:3306/pytest?useSSL=true";
	private String userName = "root";
	private String password = "www";
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
	/**
	 * 
	 * @Title createPStatement
	 * @Description 创建PrepareStatement查询对象
	 * @param String sql 待查询的SQL语句
	 * @return void
	 */
	public void createPStatement(String sql){
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Title executeQuery
	 * @Description 执行查询语句
	 * @param String[] param SQL语句中的参数数组
	 * @return ResultSet
	 */
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
	/**
	 * 
	 * @Title executeUpdate
	 * @Description 执行更新语句
	 * @param String[] param SQL语句中的参数数组
	 * @return ResultSet
	 */
	public ResultSet executeUpdate(String[] param){
		try{
			for(int i=1;i<=param.length;i++)
				stmt.setString(i, param[i-1]);

			stmt.executeUpdate();
			return stmt.getGeneratedKeys();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @Title close
	 * @Description 关闭PrepareStatement对象和数据库连接
	 * @param 
	 * @return void
	 */
	public void close(){
		try{
			stmt.close();
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

}
