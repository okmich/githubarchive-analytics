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
public interface UserDB {

	boolean exist(long	 login);

	void saveUser(GHUser user);

	void saveUserFollower(long user, long followerId);

	void saveUserToVisit(long userId, String userLogin);

	String findOneUnvisitedUser();

	void doneVisitingUser(long login);

}
