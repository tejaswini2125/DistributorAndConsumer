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

public class SaleOrderDAO {

	private OracleConnection oracleCon = new OracleConnection();

	public String insertCustomer(String distributorId, String saleOrderId, LocalDate saleDate,
			List<Map<String, Object>> saleItemsByQuantity) {
		Connection con = oracleCon.getDBConnection();
		String result = "success";
		try {
			PreparedStatement pStatement = con.prepareStatement("INSERT INTO TEJU.ORDER_DETAILS "
					+ "( DISTRIBUTORS_ID, ORDERED_DATE, EXPECTED_DELIVERY_DATE, PRODUCT_ID, QUANTITY, order_id) "
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
	
	public String getSaleOrderDetails(String distributorId, LocalDate orderDate) {
		Connection con = oracleCon.getDBConnection();
		ResultSet executeQueryResult = null;
		String errorMessage = "";
		try {
			PreparedStatement pStatement = con.prepareStatement("SELECT ORDER_ID, DISTRIBUTORS_ID, ORDERED_DATE, EXPECTED_DELIVERY_DATE, PRODUCT_ID, QUANTITY "
					+ "FROM TEJU.ORDER_DETAILS WHERE DISTRIBUTORS_ID = ? AND ORDERED_DATE = ?");
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
	
	public String getSaleOrderSummary(LocalDate orderDate) {
		Connection con = oracleCon.getDBConnection();
		ResultSet executeQueryResult = null;
		String errorMessage = "";
		try {
			PreparedStatement pStatement = con.prepareStatement("SELECT b.NAME, sum(QUANTITY) as Quantity, sum(QUANTITY * SALE_PRICE) as amount " + 
					"FROM TEJU.ORDER_DETAILS a  " + 
					"join TEJU.PRODUCTS b on a.PRODUCT_ID=b.ID  " + 
					"where ORDERED_DATE = ? " +
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
					"LEFT JOIN (SELECT DISTRIBUTORS_ID FROM TEJU.ORDER_DETAILS b WHERE ORDERED_DATE = ?) b on a.DISTRIBUTOR_ID=b.DISTRIBUTORS_ID \r\n" + 
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
	
	public String totalBillSummary(String distributorId, LocalDate orderDate) {
		Connection con = oracleCon.getDBConnection();
		ResultSet executeQueryResult = null;
		String errorMessage = "";
		try {
			PreparedStatement pStatement = con.prepareStatement("select p.Name,p.sale_price,p.mrp_price, coalesce(a.quantity,0) \"Assign quantity\", coalesce(b.quantity, 0) \"Return quantity\", coalesce(a.quantity,0) * p.sale_price - coalesce(b.quantity, 0)*p.sale_price \"Total Amount\" from teju.return_details b " + 
					"full join teju.assign_details a on a.distributors_id = b.distributors_id and a.product_id=b.product_id and a.assign_date = b.return_date " + 
					"join teju.products p on coalesce(a.product_id, b.product_id) = p.id " + 
					"Where coalesce(a.distributors_id, b.distributors_id) = ? " + 
					"and coalesce(a.assign_date, b.return_date) = ?");
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

}
