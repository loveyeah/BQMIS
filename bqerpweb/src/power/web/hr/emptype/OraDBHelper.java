package power.web.hr.emptype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class OraDBHelper { 
//	public static Connection getConn(){ 
//		final String url  = "jdbc:oracle:thin:@127.0.0.1:1521:pmisdb";
//		final String user = "eamadmin";
//		final String pwd  = "eamadmin"; 
//		Connection conn = null;
//	    try{
//	      Class.forName("oracle.jdbc.driver.OracleDrivoer");
//	      conn=DriverManager.getConnection(url,user,pwd);
//	    }catch(Exception e){
//	      System.out.print("ȡ�����l�ӳ��!! ");
//	    }
//	    return conn; 
//	} 
	/**
	 * ִ���޲����sql���
	 * @param sql
	 * @return Ӱ�������
	 * @throws SQLException
	 */
	public static int executeSql(String sql) throws SQLException
	{ 
		Connection conn = getConn();
		PreparedStatement pstmt = null;
		try
		{ 
			pstmt = conn.prepareStatement(sql);
			return pstmt.executeUpdate();   
		}
		finally{
			if(pstmt !=null){
				pstmt.close();
			}
			if(conn !=null){
				conn.close();
			}
		}
	}
	public static Object getSingle(String sql) throws SQLException
	{  
		Connection conn = getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery(); 
			if(rs.next()){
				return rs.getObject(1);
			}
		}finally{
			if(rs!=null){
				rs.close();  
			}
			if(pstmt !=null){
				pstmt.close();
			}
			if(conn !=null){
				conn.close();
			}
		}
		return null; 
	}
	//	ȡ�����Դ
	 public static Connection getConn(){ 
		DataSource datasource = null;
		Connection conn = null;
		try { 
			final String location = "java:/eaiDS";
			InitialContext ic = new InitialContext();
			datasource = (DataSource)ic.lookup(location); 
			conn = datasource.getConnection();
		} catch (NamingException ne) {    
			System.out.println("ȡ�����Դ����!");
		}catch(SQLException e){
			System.out.println("ȡ����ݿ�l�Ӵ���!");
		} 
		return conn;
	} 
}
