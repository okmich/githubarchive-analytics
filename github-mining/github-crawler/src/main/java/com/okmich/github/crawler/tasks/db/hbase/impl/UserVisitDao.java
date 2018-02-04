/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler.tasks.db.hbase.impl;

import static com.okmich.github.crawler.tasks.db.hbase.impl.Util.*;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;

/**
 *
 * @author michael.enudi
 */
public class UserVisitDao {

	private final Table table;
	/**
	 * COLUMN_FAMILY_MAIN
	 */
	public static final byte[] COLUMN_FAMILY_LOG = as("log");
	/**
	 * USER_ID
	 */
	public static final byte[] USER_ID = as("id");

	/**
	 * USER_LOGIN
	 */
	public static final byte[] USER_LOGIN = as("login");
	/**
	 * STATUS
	 */
	public static final byte[] STATUS = as("status");
	/**
	 * TIMESTAMP
	 */
	public static final byte[] TIMESTAMP = as("ts");

	/**
     *
     *
     */
	public UserVisitDao() {
		Configuration conf = HBaseConfiguration.create();
		conf.set(HConstants.ZOOKEEPER_QUORUM,
				DBConstants.HBASE_ZOOKEEPER_QUORUM);
		conf.set(HConstants.ZOOKEEPER_CLIENT_PORT,
				DBConstants.HBASE_ZOOKEEPER_CLIENT_PORT);
		try {
			Connection connection = ConnectionFactory.createConnection(conf);
			this.table = connection.getTable(TableName
					.valueOf(DBConstants.USER_LOG_TABLE));
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 *
	 * @param userLogin
	 * @throws Exception
	 */
	public void addToVisitLog(long userId, String userLogin) throws Exception {
		Put put = new Put(as(userId));

		put.addColumn(COLUMN_FAMILY_LOG, USER_LOGIN, as(userLogin));
		put.addColumn(COLUMN_FAMILY_LOG, STATUS, as("NOT"));
		put.addColumn(COLUMN_FAMILY_LOG, TIMESTAMP,
				as(System.currentTimeMillis()));
		try {
			table.put(put);
		} catch (IOException e) {
			throw new Exception(e.getMessage(), e);
		}
	}

	/**
	 *
	 * @param userLogin
	 * @throws Exception
	 */
	public void markAsVisited(long userLogin) throws Exception {
		Delete delete = new Delete(as(userLogin));

		try {
			table.delete(delete);
		} catch (IOException e) {
			throw new Exception(e.getMessage(), e);
		}
	}

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	public String getNextRandomly() throws Exception {
		Scan scan = new Scan();
		scan.addFamily(COLUMN_FAMILY_LOG);

		ResultScanner resultScanner = table.getScanner(scan);
		String _status;
		String userLogin = "";
		Long userId = -1L;
		for (Result result : resultScanner) {
			userId = getLong(result.getRow());
			userLogin = getString(result
					.getValue(COLUMN_FAMILY_LOG, USER_LOGIN));
			_status = getString(result.getValue(COLUMN_FAMILY_LOG, STATUS));
			if (_status.equals("NOT")) {
				break;
			}
		}
		if (userLogin.isEmpty()) {
			return null;
		}

		Put put = new Put(as(userId));

		put.addColumn(COLUMN_FAMILY_LOG, STATUS, as("PENDING"));
		put.addColumn(COLUMN_FAMILY_LOG, TIMESTAMP,
				as(System.currentTimeMillis()));
		try {
			table.put(put);
		} catch (IOException e) {
			throw new Exception(e.getMessage(), e);
		}

		return userLogin;
	}
}
