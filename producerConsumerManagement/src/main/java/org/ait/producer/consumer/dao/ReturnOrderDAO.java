package org.ait.producer.consumer.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.ait.producer.consumer.db.OracleConnection;
import org.ait.producer.consumer.utils.ResultConversionUtils;

public class ReturnOrderDAO {

	private OracleConnection oracleCon = new OracleConnection();

	public String insertReturnOrder(String distributorId, String saleOrderId, LocalDate saleDate,
			List<Map<String, Object>> saleItemsByQuantity) {
		Connection con = oracleCon.getDBConnection();
		String result = "success";
		try {
			PreparedStatement pStatement = con.prepareStatement("INSERT INTO TEJU.RETURN_DETAILS "
					+ "( DISTRIBUTORS_ID, RETURN_DATE, EXPECTED_DELIVERY_DATE, PRODUCT_ID, QUANTITY, RETURN_ID) "
					+ "VALUES (?, ?, ?, ?, ?, ?)");
			for (Map<String, Object> items : saleItemsByQuantity) {
				pStatement.setString(1, distributorId);
				pStatement.setDate(2, Date.valueOf(saleDate));
				pStatement.setDate(3, Date.valueOf(saleDate.plusDays(1)));
				pStatement.setString(4, (String) items.get("productId"));
				pStatement.setInt(5, (int) items.get("quantity"));
				pStatement.setString(6, saleOrderId);
				pStatement.executeUpdate();				
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			return "{'errorMessage': 'The order is already placed'}";
		} catch(Exception e) {
			e.printStackTrace();
			return "{'errorMessage': '"+ e.getMessage()+"'}";
		} finally {
			oracleCon.close(con);
		}
		return result;
	}
	
	public String getReturnOrderDetails(String distributorId, LocalDate orderDate) {
		Connection con = oracleCon.getDBConnection();
		ResultSet executeQueryResult = null;
		String errorMessage = "";
		try {
			PreparedStatement pStatement = con.prepareStatement("SELECT RETURN_ID, DISTRIBUTORS_ID, RETURN_DATE, PRODUCT_ID, QUANTITY "
					+ "FROM TEJU.RETURN_DETAILS WHERE DISTRIBUTORS_ID = ? AND RETURN_DATE = ?");
			pStatement.setString(1, distributorId);
			pStatement.setDate(2, Date.valueOf(orderDate));
			executeQueryResult = pStatement.executeQuery();
			return ResultConversionUtils.convertJson(executeQueryResult);
		} catch (SQLException e) {
			e.printStackTrace();
			errorMessage ="{'errorMessage': '"+ e.getMessage()+"'}";
		} finally {
			oracleCon.close(executeQueryResult);
			oracleCon.close(con);
		}
		return errorMessage;
	}
	
	public String getReturnOrderSummary(LocalDate orderDate) {
		Connection con = oracleCon.getDBConnection();
		ResultSet executeQueryResult = null;
		String errorMessage = "";
		try {
			PreparedStatement pStatement = con.prepareStatement("SELECT b.NAME, sum(QUANTITY) as Quantity, sum(QUANTITY * SALE_PRICE) as amount " + 
					"FROM TEJU.RETURN_DETAILS a  " + 
					"join TEJU.PRODUCTS b on a.PRODUCT_ID=b.ID  " + 
					"where RETURN_DATE = ? " +
					"group by b.NAME ");
			pStatement.setDate(1, Date.valueOf(orderDate));
			executeQueryResult = pStatement.executeQuery();
			return ResultConversionUtils.convertJson(executeQueryResult);
		} catch (SQLException e) {
			e.printStackTrace();
			errorMessage ="{'errorMessage': '"+ e.getMessage()+"'}";
		} finally {
			oracleCon.close(executeQueryResult);
			oracleCon.close(con);
		}
		return errorMessage;
	}
	
	public String distributorMissedOrder(LocalDate orderDate) {
		Connection con = oracleCon.getDBConnection();
		ResultSet executeQueryResult = null;
		String errorMessage = "";
		try {
			PreparedStatement pStatement = con.prepareStatement("SELECT a.DISTRIBUTOR_ID, a.NAME, a.EMAIL, a.PHONE_NUMBER " + 
					"FROM TEJU.DISTRIBUTORS a " + 
					"LEFT JOIN (SELECT DISTRIBUTORS_ID FROM TEJU.RETURN_DETAILS b WHERE RETURN_DATE = ?) b on a.DISTRIBUTOR_ID=b.DISTRIBUTORS_ID \r\n" + 
					"WHERE b.DISTRIBUTORS_ID is null " + 
					"group by a.DISTRIBUTOR_ID, a.NAME, a.EMAIL, a.PHONE_NUMBER ");
			pStatement.setDate(1, Date.valueOf(orderDate));
			executeQueryResult = pStatement.executeQuery();
			return ResultConversionUtils.convertJson(executeQueryResult);
		} catch (SQLException e) {
			e.printStackTrace();
			errorMessage ="{'errorMessage': '"+ e.getMessage()+"'}";
		} finally {
			oracleCon.close(executeQueryResult);
			oracleCon.close(con);
		}
		return errorMessage;
	}

}
