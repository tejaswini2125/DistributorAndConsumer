package org.ait.producer.consumer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.ait.producer.consumer.db.OracleConnection;
import org.ait.producer.consumer.model.Customer;

public class CustomerDAO {
	private OracleConnection oracleCon = new OracleConnection();

	public void insertCustomer(Customer customer) {
		Connection con = oracleCon.getDBConnection();
		try {
			PreparedStatement pStatement = con.prepareStatement("insert into "
					+ "teju.customer(CUSTOMER_ID,CUSTOMER_NAME,DATE_OF_ACTIVATION,ADDRESS_PROOF_ID,ADDRESS_PROOF_TYPE,EMAIL_ID) "
					+ "values (?,?,?,?,?,?)");
			pStatement.setInt(0, customer.getCustomerId());
			pStatement.setString(1, customer.getCustomerName());
			pStatement.setDate(2, customer.getDateOfActivation());
			pStatement.setString(3, customer.getAddressProofId());
			pStatement.setString(4, customer.getAddressProofType());
			pStatement.setString(5, customer.getEmailId());
			pStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			oracleCon.close(con);
		}
		
	}
}
