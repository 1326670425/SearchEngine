package database;
import java.sql.*;
import javax.naming.*;
import javax.sql.DataSource;

/**
 * @ClassName DbConnPool
 * @Description ���ݿ����ӳ�
 * @author �����
 * @date 2018��6��4��
 * 
 */
public class DbConnPool {
	private static DataSource dataSource;
	private static Connection conn;
	/**
	 * 
	 * @Title getConnection
	 * @Description ������ݿ�����
	 * @param 
	 * @return Connection
	 * @throws
	 */
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
