/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler.tasks.db.jdbc.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author michael.enudi
 */
public class UserVisitDao extends BaseDao {

	private final PreparedStatement insertPreStatement;
	private final PreparedStatement markAsVisitedPreStatement;
	private final PreparedStatement markAsPendingPreStatement;
	private final PreparedStatement randomSelectPreStatement;

	private static final String INSERT_QUERY = "INSERT INTO visit_log VALUES(?, ? ,?, ?)";

	private static final String MARK_PENDING_QUERY = "UPDATE visit_log SET status = 'PENDING' WHERE id = ?";

	private static final String MARK_VISITED_QUERY = "UPDATE visit_log SET status = 'DONE' WHERE id = ?";

	private static final String RANDOM_SELECT_QUERY = "SELECT id, login FROM visit_log WHERE status = 'NOT' ORDER BY RAND() LIMIT 1";

	/**
     *
     *
     */
	public UserVisitDao() {
		super();
		try {
			this.insertPreStatement = prepareStatement(INSERT_QUERY);
			this.markAsVisitedPreStatement = prepareStatement(MARK_VISITED_QUERY);
			this.markAsPendingPreStatement = prepareStatement(MARK_PENDING_QUERY);
			this.randomSelectPreStatement = prepareStatement(RANDOM_SELECT_QUERY);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 *
	 * @param userLogin
	 * @throws Exception
	 */
	public void addToVisitLog(long userId, String userLogin) throws Exception {
		insertPreStatement.setLong(1, userId);
		insertPreStatement.setString(2, userLogin);
		insertPreStatement.setString(3, "NOT");
		insertPreStatement.setLong(4, System.currentTimeMillis());

		try {
			insertPreStatement.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *
	 * @param userLogin
	 * @throws Exception
	 */
	public void markAsVisited(long userLogin) throws Exception {
		markAsVisitedPreStatement.setLong(1, userLogin);

		try {
			markAsVisitedPreStatement.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	public String getNextRandomly() throws Exception {
		long id = -1;
		String login = null;

		ResultSet rs = this.randomSelectPreStatement.executeQuery();
		while (rs.next()) {
			login = rs.getString("login");
			id = rs.getLong("id");
		}

		if (id > -1) {
			markAsPendingPreStatement.setLong(1, id);

			try {
				markAsPendingPreStatement.executeUpdate();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		
		return login;
	}
}
