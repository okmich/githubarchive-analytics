package com.okmich.github.crawler.tasks.db.jdbc.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class BaseDao {

	private final Connection connection;

	public BaseDao() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(
					JDBConstants.JDBC_URL, JDBConstants.JDBC_USER,
					JDBConstants.JDBC_PASSWORD);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public PreparedStatement prepareStatement(String query) throws SQLException {
		return this.connection.prepareStatement(query);
	}
}
