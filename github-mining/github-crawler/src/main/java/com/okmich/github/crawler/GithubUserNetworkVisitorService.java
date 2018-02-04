/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.okmich.github.crawler.tasks.UserNetworkVisitorTask;
import com.okmich.github.crawler.tasks.UserTask;
import com.okmich.github.crawler.tasks.db.UserDB;

/**
 *
 * @author michael.enudi
 */
public class GithubUserNetworkVisitorService {

	private static final Logger LOG = Logger
			.getLogger(GithubUserNetworkVisitorService.class.getName());

	private final ScheduledExecutorService scheduledExecutorService;
	private UserDB userDB;

	/**
	 *
	 * @param userDB
	 */
	public GithubUserNetworkVisitorService(UserDB userDB) {
		this.userDB = userDB;
		this.scheduledExecutorService = Executors.newScheduledThreadPool(2);
	}

	public void start() {
		UserTask userTask = new UserNetworkVisitorTask(userDB);

		this.scheduledExecutorService.scheduleAtFixedRate(userTask, 0, 20,
				TimeUnit.SECONDS);
	}

	public void stop() {
		this.scheduledExecutorService.shutdown();
		try {
			this.scheduledExecutorService.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException ex) {
			LOG.log(Level.SEVERE, null, ex);
			stopNow();
		}
	}

	public void stopNow() {
		this.scheduledExecutorService.shutdownNow();
	}
}
