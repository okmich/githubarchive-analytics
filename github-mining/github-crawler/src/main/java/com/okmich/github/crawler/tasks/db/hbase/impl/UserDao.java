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
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.kohsuke.github.GHUser;

/**
 *
 * @author michael.enudi
 */
public class UserDao {

    private final Table table;
    /**
     * COLUMN_FAMILY_USER
     */
    public static final byte[] COLUMN_FAMILY_USER = as("u");
    /**
     * ID
     */
    public static final byte[] ID = as("id");
    /**
     * LOGIN
     */
    public static final byte[] LOGIN = as("login");
    /**
     * NAME
     */
    public static final byte[] NAME = as("name");
    /**
     * LOCATION
     */
    public static final byte[] LOCATION = as("location");
    /**
     * COMPANY
     */
    public static final byte[] COMPANY = as("company");
    /**
     * BLOG
     */
    public static final byte[] BLOG = as("blog");
    /**
     * BIO
     */
    public static final byte[] JOINED = as("joined");
    /**
     * TIMESTAMP
     */
    public static final byte[] TIMESTAMP = as("ts");

    /**
     *
     *
     */
    public UserDao() {
        Configuration conf = HBaseConfiguration.create();
        conf.set(HConstants.ZOOKEEPER_QUORUM, DBConstants.HBASE_ZOOKEEPER_QUORUM);
        conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, DBConstants.HBASE_ZOOKEEPER_CLIENT_PORT);
        try {
            Connection connection = ConnectionFactory.createConnection(conf);
            this.table = connection.getTable(TableName.valueOf(DBConstants.USER_TABLE));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     *
     * @param ghUser
     * @throws Exception
     */
    public void addUser(GHUser ghUser) throws Exception {
        Put put = new Put(as(ghUser.getId()));

        try {
            put.addColumn(COLUMN_FAMILY_USER, ID, as(ghUser.getId()));
            put.addColumn(COLUMN_FAMILY_USER, LOGIN, as(ghUser.getLogin()));
            put.addColumn(COLUMN_FAMILY_USER, NAME, as(ghUser.getName()));
            put.addColumn(COLUMN_FAMILY_USER, LOCATION, as(ghUser.getLocation()));
            put.addColumn(COLUMN_FAMILY_USER, COMPANY, as(ghUser.getCompany()));
            put.addColumn(COLUMN_FAMILY_USER, BLOG, as(ghUser.getBlog()));
            put.addColumn(COLUMN_FAMILY_USER, JOINED, as(ghUser.getCreatedAt()));
            put.addColumn(COLUMN_FAMILY_USER, TIMESTAMP, as(System.currentTimeMillis()));

            table.put(put);
        } catch (IOException e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public String findUser(long id) throws Exception {
        Get get = new Get(as(id));

        try {
            Result result = table.get(get);
            byte[] bytes = result.getValue(COLUMN_FAMILY_USER, ID);
            if (bytes == null) {
                return null;
            } else {
                return getString(bytes);
            }
        } catch (IOException e) {
            throw new Exception(e.getMessage(), e);
        }
    }

}
