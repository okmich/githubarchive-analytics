/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler.tasks.db.jdbc.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.kohsuke.github.GHUser;

import com.okmich.github.crawler.tasks.db.hbase.impl.Util;

/**
 *
 * @author michael.enudi
 */
public class UserDao extends BaseDao {

	private final PreparedStatement insertPreStatement;
	private final PreparedStatement selectOnePreStatement;

	private static final String INSERT_QUERY = "INSERT INTO user VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT_ONE_QUERY = "SELECT login FROM user WHERE id = ?";

	/**
     *
     *
     */
	public UserDao() {
		super();
		try {
			this.insertPreStatement = prepareStatement(INSERT_QUERY);
			this.selectOnePreStatement = prepareStatement(SELECT_ONE_QUERY);
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 *
	 * @param ghUser
	 * @throws Exception
	 */
	public void addUser(GHUser ghUser) throws Exception {
		insertPreStatement.setLong(1, ghUser.getId());
		insertPreStatement.setString(2, ghUser.getLogin());
		insertPreStatement.setString(3, ghUser.getName());
		insertPreStatement.setString(4, ghUser.getLocation());
		insertPreStatement.setString(5, ghUser.getCompany());
		insertPreStatement.setString(6, ghUser.getBlog());
		insertPreStatement.setString(7, Util.DF.format(ghUser.getCreatedAt()));
		insertPreStatement.setLong(8, System.currentTimeMillis());

		try {
			insertPreStatement.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String findUser(long id) throws Exception {
		selectOnePreStatement.setLong(1, id);

		ResultSet rs = selectOnePreStatement.executeQuery();

		while (rs.next())
			return rs.getString("login");

		return null;
	}

}
