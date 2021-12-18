package org.ait.producer.consumer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.ait.producer.consumer.db.OracleConnection;
import org.ait.producer.consumer.model.CDRDetail;

public class CDRDetailDAO {

	private OracleConnection oracleCon = new OracleConnection();

	public void insertCustomer(CDRDetail cdrDetail) {
		Connection con = oracleCon.getDBConnection();
		try {
			PreparedStatement pStatement = con.prepareStatement("insert into "
					+ "teju.customer(FROM_MOBILE_NO,TO_MOBILE_NO,AREA_CODE,CALL_TYPE,STAR_TIME,END_TIME,COST,DURATION) "
					+ "values (?,?,?,?,?,?)");
			pStatement.setString(0, cdrDetail.getFromMobileNo());
			pStatement.setString(1, cdrDetail.getToMobileNo());
			pStatement.setString(2, cdrDetail.getAreaCode());
			pStatement.setString(3, cdrDetail.getCallType());
			pStatement.setDate(4, cdrDetail.getStarTime());
			pStatement.setDate(5, cdrDetail.getEndTime());
			pStatement.setFloat(6, cdrDetail.getCost());
			pStatement.setLong(7, cdrDetail.getDuration());
			pStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			oracleCon.close(con);
		}

	}

}
