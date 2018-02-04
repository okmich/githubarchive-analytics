/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael.enudi
 */
public abstract class UserTask implements Runnable {

	/**
     *
     */
	private static final Logger LOG = Logger
			.getLogger(UserTask.class.getName());

	public abstract void perform();

	@Override
	public void run() {
		try {
			perform();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
