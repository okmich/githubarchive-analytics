/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler.tasks.db;

import org.kohsuke.github.GHUser;

/**
 *
 * @author michael.enudi
 */
public class MockUserDBImpl implements UserDB {

	public MockUserDBImpl() {
	}

	@Override
	public boolean exist(long login) {
		return true;
	}

	@Override
	public void saveUser(GHUser user) {
		System.out.println("saving details of user " + user);
	}

	@Override
	public void saveUserFollower(long user, long followers) {
		System.out
				.println("saving: " + followers + " is a follower of " + user);
	}

	@Override
	public void saveUserToVisit(long userId, String userLogin) {
		System.out.println("saving details of user to be visited " + userLogin);
	}

	@Override
	public void doneVisitingUser(long login) {
		System.out.println("clear user as having been visited " + login);
	}

	@Override
	public String findOneUnvisitedUser() {
		return "okmich";
	}

}
