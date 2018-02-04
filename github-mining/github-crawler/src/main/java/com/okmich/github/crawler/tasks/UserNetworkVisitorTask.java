/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler.tasks;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

import com.okmich.github.crawler.tasks.db.UserDB;

/**
 *
 * @author michael.enudi
 */
public class UserNetworkVisitorTask extends UserTask {

	/**
     *
     */
	private static final Logger LOG = Logger
			.getLogger(UserNetworkVisitorTask.class.getName());
	private GHUser ghUser;
	private UserDB userDB;
	private GitHub gitHub;

	/**
	 *
	 * @param userLogin
	 */
	public UserNetworkVisitorTask(UserDB userDB) {
		this.userDB = userDB;

		try {
			this.gitHub = GitHub
					.connectUsingOAuth("...put your access token here .....");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void perform() {
		try {

			LOG.log(Level.INFO, "Running..");

			String unvisitedUser = userDB.findOneUnvisitedUser();
			if (unvisitedUser == null || unvisitedUser.isEmpty()) {
				return;
			}
			this.ghUser = this.gitHub.getUser(unvisitedUser);

			LOG.log(Level.INFO,
					"Found User {0} with followers {1} and follows {2}",
					new Object[] { this.ghUser.getId(),
							this.ghUser.getFollowersCount(),
							this.ghUser.getFollowingCount() });

			visitGHUser();
			LOG.log(Level.INFO, "Network of User {0} has been visited",
					this.ghUser.getId());
			// set the user reference to null
			this.ghUser = null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public GHUser getGHUser(String userLogin) {
		try {
			this.ghUser = this.gitHub.getUser(userLogin);
			return this.ghUser;
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
     *
     */
	private void visitGHUser() {
		PagedIterable<GHUser> followers = ghUser.listFollowers().withPageSize(
				20);
		long userId;
		for (GHUser follower : followers) {
			userId = follower.getId();
			// check if this guy exist
			if (!userDB.exist(userId)) {
				userDB.saveUserToVisit(userId, follower.getLogin());
				saveUser(follower);
			}
			saveConnection(userId, true);
		}
		PagedIterable<GHUser> followes = ghUser.listFollows().withPageSize(20);
		for (GHUser follower : followes) {
			userId = follower.getId();
			// check if this guy exist
			if (!userDB.exist(userId)) {
				userDB.saveUserToVisit(userId, follower.getLogin());
				saveUser(follower);
			}
			saveConnection(userId, false);
		}
		// done
		userDB.doneVisitingUser(ghUser.getId());
	}

	private void saveConnection(long secondaryUser, boolean isFollower) {
		if (isFollower) {
			userDB.saveUserFollower(ghUser.getId(), secondaryUser);
		} else {
			userDB.saveUserFollower(secondaryUser, ghUser.getId());
		}
	}

	private void saveUser(GHUser user) {
		try {
			this.userDB.saveUser(user);
		} catch (Exception ex) {
			LOG.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
}
