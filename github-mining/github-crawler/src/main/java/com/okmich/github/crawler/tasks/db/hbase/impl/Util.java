/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler.tasks.db.hbase.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author michael.enudi
 */
public class Util {

	public static final DateFormat DF = new SimpleDateFormat(
			"yyyyMMdd hh:mm:ss Z");

	public static byte[] as(String obj) {
		if (obj == null)
			return null;
		else
			return Bytes.toBytes(obj);
	}

	public static byte[] as(Long obj) {
		if (obj == null)
			return null;
		else
			return Bytes.toBytes(obj);
	}

	public static byte[] as(Date obj) {
		if (obj == null)
			return null;
		else
			return Bytes.toBytes(DF.format(obj));
	}

	public static String getString(byte[] obj) {
		if (obj == null)
			return null;
		else
			return Bytes.toString(obj);
	}

	public static Long getLong(byte[] obj) {
		if (obj == null)
			return null;
		else
			return Bytes.toLong(obj);
	}
}
