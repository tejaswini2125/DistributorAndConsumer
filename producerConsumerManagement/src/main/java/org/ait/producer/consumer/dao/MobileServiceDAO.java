package org.ait.producer.consumer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.ait.producer.consumer.db.OracleConnection;
import org.ait.producer.consumer.model.MobileService;

public class MobileServiceDAO {
	private OracleConnection oracleCon = new OracleConnection();

	public void insertMobileService(MobileService mobileService) {
		Connection con = oracleCon.getDBConnection();
		try {
			PreparedStatement pStatement = con.prepareStatement("insert into "
					+ "teju.MOBILE_SERVICE(MOBILE_NO,CUSTOMER_ID,TARIFF_ID,BALANCE) "
					+ "values (?,?,?,?)");
			pStatement.setString(0, mobileService.getMobileNo());
			pStatement.setInt(1, mobileService.getCustomerId());
			pStatement.setInt(2, mobileService.getTariffId());
			pStatement.setFloat(3, mobileService.getBalance());
			pStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			oracleCon.close(con);
		}
		
	}

}
