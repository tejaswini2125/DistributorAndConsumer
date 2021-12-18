package org.ait.producer.consumer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.ait.producer.consumer.utils.ResultConversionUtils;

public class OracleConnection {

	public String execute(String query) {
		String res = null;
		ResultSet rs = null;
		Connection con = getDBConnection();
		Statement stmt;
		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery(query);
			res = ResultConversionUtils.convertTable(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(con);
			close(rs);
		}

		return res;
	}
	
	public void close(Connection closableObject) {
		if(closableObject != null) {
			try {
				closableObject.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close(ResultSet closableObject) {
		if(closableObject != null) {
			try {
				closableObject.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Connection getDBConnection() {
		Connection con = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "sys as sysdba", "root123");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}


}
