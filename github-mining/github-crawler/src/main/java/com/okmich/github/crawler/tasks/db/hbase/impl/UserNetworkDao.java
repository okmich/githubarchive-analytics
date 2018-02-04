/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler.tasks.db.hbase.impl;

import static com.okmich.github.crawler.tasks.db.hbase.impl.Util.as;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;

/**
 *
 * @author michael.enudi
 */
public class UserNetworkDao {

    private final Table table;
    /**
     * COLUMN_FAMILY_MAIN
     */
    public static final byte[] COLUMN_FAMILY_MAIN = as("main");
    /**
     * PRIMARY
     */
    public static final byte[] PRIMARY = as("p");
    /**
     * FOLLOWER
     */
    public static final byte[] FOLLOWER = as("f");
    /**
     * TIMESTAMP
     */
    public static final byte[] TIMESTAMP = as("ts");

    /**
     *
     *
     */
    public UserNetworkDao() {
        Configuration conf = HBaseConfiguration.create();
        conf.set(HConstants.ZOOKEEPER_QUORUM, DBConstants.HBASE_ZOOKEEPER_QUORUM);
        conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, DBConstants.HBASE_ZOOKEEPER_CLIENT_PORT);
        try {
            Connection connection = ConnectionFactory.createConnection(conf);
            this.table = connection.getTable(TableName.valueOf(DBConstants.USER_NETWORK_TABLE));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void saveUserFollowing(long primary, long follower) throws Exception {
        Put put = new Put(as(primary + "-" + follower));

        put.addColumn(COLUMN_FAMILY_MAIN, PRIMARY, as(primary));
        put.addColumn(COLUMN_FAMILY_MAIN, FOLLOWER, as(follower));
        put.addColumn(COLUMN_FAMILY_MAIN, TIMESTAMP, as(System.currentTimeMillis()));
        try {
            table.put(put);
        } catch (IOException e) {
            throw new Exception(e.getMessage(), e);
        }
    }
}
