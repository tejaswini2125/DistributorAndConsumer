package org.ait.producer.consumer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.ait.producer.consumer.db.OracleConnection;
import org.ait.producer.consumer.utils.ResultConversionUtils;

public class ProductDAO {

	private OracleConnection oracleCon = new OracleConnection();

	public String getAllProductDetails() {
		Connection con = oracleCon.getDBConnection();
		ResultSet executeQuery = null;
		String errorMessage = "";
		try {
			PreparedStatement pStatement = con.prepareStatement("select id, name \"Product Name\", sale_price \"Sale Price\", mrp_price \"MRP Price\" from teju.products");
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
