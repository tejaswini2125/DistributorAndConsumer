package org.ait.producer.consumer.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultConversionUtils {

	public final static String SUCCESS = "success";

	public static String convertTable(ResultSet resultSet) {
		ResultSetMetaData rsmd;
		String tableStructure = "<table>";
		try {
			rsmd = resultSet.getMetaData();
			int columnCount = rsmd.getColumnCount();
			tableStructure += "<tr>";
			for (int i = 1; i <= columnCount; i++) {
				tableStructure += "<th>" + rsmd.getColumnName(i).toLowerCase() + "</th>";
			}
			tableStructure += "</tr>";
			while (resultSet.next()) {
				tableStructure += "<tr>";
				for (int i = 1; i <= columnCount; i++) {
					tableStructure += "<td>" + resultSet.getString(i) + "</td>";
				}
				tableStructure += "</tr>";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		tableStructure += "</table>";
		return tableStructure;
	}

	public static String convertJson(ResultSet resultSet) {
		ResultSetMetaData rsmd;
		String tableStructure = "{";
		try {
			rsmd = resultSet.getMetaData();
			int columnCount = rsmd.getColumnCount();
			tableStructure += "\"columns\":[";
			for (int i = 1; i <= columnCount; i++) {
				if (i != 1) {
					tableStructure += ",";
				}
				tableStructure += "\"" + rsmd.getColumnName(i).toLowerCase() + "\"";
			}
			tableStructure += "],";
			tableStructure += "\"rows\":[";
			int count = 1;
			while (resultSet.next()) {
				if (count++ != 1) {
					tableStructure += ",";
				}
				tableStructure += "{";
				for (int i = 1; i <= columnCount; i++) {
					if (i != 1) {
						tableStructure += ",";
					}
					tableStructure += "\"" + rsmd.getColumnName(i).toLowerCase() + "\" :" + "\""
							+ resultSet.getString(i) + "\"";
				}
				tableStructure += "}";
			}
			tableStructure += "]";

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		tableStructure += "}";
		return tableStructure;
	}
}
