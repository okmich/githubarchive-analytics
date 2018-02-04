/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler.tasks.db.hbase.impl;

/**
 *
 * @author michael.enudi
 */
public interface DBConstants {

    String HBASE_ZOOKEEPER_QUORUM = "quickstart.cloudera";
    String HBASE_ZOOKEEPER_CLIENT_PORT = "2181";

    String USER_TABLE = "github:user";
    String USER_NETWORK_TABLE = "github:user_network";
    String USER_LOG_TABLE = "github:user_visit_log";
}
