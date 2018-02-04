/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler.tasks.db.jdbc.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author michael.enudi
 */
public class UserNetworkDao extends BaseDao {

	private final PreparedStatement insertPreStatement;

	private static final String INSERT_QUERY = "INSERT INTO user_network VALUES(?, ? ,?)";

	/**
     *
     *
     */
	public UserNetworkDao() {
		super();
		try {
			this.insertPreStatement = prepareStatement(INSERT_QUERY);
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void saveUserFollowing(long primary, long follower) throws Exception {
		insertPreStatement.setLong(1, primary);
		insertPreStatement.setLong(2, follower);
		insertPreStatement.setLong(3, System.currentTimeMillis());

		try {
			insertPreStatement.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
}
