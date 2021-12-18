package org.ait.producer.consumer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.ait.producer.consumer.db.OracleConnection;
import org.ait.producer.consumer.utils.ResultConversionUtils;

public class DistributorDao {

	private OracleConnection oracleCon = new OracleConnection();

	public String login(String userId, String pwd) {
		Connection con = oracleCon.getDBConnection();
		ResultSet executeQuery = null;
		String loginMessage = "";
		try {
			PreparedStatement pStatement = con.prepareStatement("select distributor_id,name,email from teju.distributors where email=? and password=?");
			pStatement.setString(1, userId);
			pStatement.setString(2, pwd);
			executeQuery = pStatement.executeQuery();
			if(executeQuery.next()) {
				loginMessage = getLoginDetails(userId);
			} else {
				loginMessage = "{'errorMessage': 'User Id or Password is wrong'}";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			loginMessage = e.getMessage();
		} finally {
			oracleCon.close(executeQuery);
			oracleCon.close(con);
		}
		return loginMessage;
	}
	
	public String getLoginDetails(String userId) {
		Connection con = oracleCon.getDBConnection();
		ResultSet executeQuery = null;
		String errorMessage = "";
		try {
			PreparedStatement pStatement = con.prepareStatement("select distributor_id,name,email from teju.distributors where email=?");
			pStatement.setString(1, userId);
			executeQuery = pStatement.executeQuery();
			return ResultConversionUtils.convertJson(executeQuery);
		} catch (SQLException e) {
			e.printStackTrace();
			errorMessage ="{'errorMessage': '"+ e.getMessage()+"'}";
		} finally {
			oracleCon.close(executeQuery);
			oracleCon.close(con);
		}
		return errorMessage;
	}
	
	public String getAllDistributors() {
		Connection con = oracleCon.getDBConnection();
		ResultSet executeQuery = null;
		String errorMessage = "";
		try {
			PreparedStatement pStatement = con.prepareStatement("select distributor_id,name from teju.distributors");
			executeQuery = pStatement.executeQuery();
			return ResultConversionUtils.convertJson(executeQuery);
		} catch (SQLException e) {
			e.printStackTrace();
			errorMessage ="{'errorMessage': '"+ e.getMessage()+"'}";
		} finally {
			oracleCon.close(executeQuery);
			oracleCon.close(con);
		}
		return errorMessage;
	}

}
