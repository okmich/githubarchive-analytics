/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler.tasks.db.jdbc.impl;

/**
 *
 * @author michael.enudi
 */
public interface JDBConstants {

	String JDBC_URL = "jdbc:mysql://quickstart.cloudera:3306/github";
	String JDBC_USER = "root";
	String JDBC_PASSWORD = "cloudera";

	String USER_TABLE = "user";
	String USER_NETWORK_TABLE = "user_network";
	String USER_LOG_TABLE = "visit_log";
}
