package database;
import java.sql.*;

/**
 * @ClassName DbOperation
 * @Description ִ�����ݿ����
 * @author �����
 * @date 2018��6��4��
 * 
 */
public class DbOperation {

	private Connection conn;
	private PreparedStatement stmt;
	//���ӳ�����
	public DbOperation(){
		conn = DbConnPool.getConnection();
	}
/*	//��ͳ��ʽ����
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
	 * @Description ����PrepareStatement��ѯ����
	 * @param String sql ����ѯ��SQL���
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
	 * @Description ִ�в�ѯ���
	 * @param String[] param SQL����еĲ�������
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
	 * @Description ִ�и������
	 * @param String[] param SQL����еĲ�������
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
	 * @Description �ر�PrepareStatement��������ݿ�����
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
