package com.okmich.github.crawler.tasks.db;

import java.util.logging.Logger;

import org.kohsuke.github.GHUser;

import com.okmich.github.crawler.tasks.db.jdbc.impl.UserDao;
import com.okmich.github.crawler.tasks.db.jdbc.impl.UserNetworkDao;
import com.okmich.github.crawler.tasks.db.jdbc.impl.UserVisitDao;

public class JDBCUserDBImpl implements UserDB {

	/**
     *
     */
	private static final Logger LOG = Logger.getLogger(JDBCUserDBImpl.class
			.getName());
	private final UserDao userDao;
	private final UserNetworkDao userNetworkDao;
	private final UserVisitDao userVisitDao;

	public JDBCUserDBImpl() {
		this.userDao = new UserDao();
		this.userNetworkDao = new UserNetworkDao();
		this.userVisitDao = new UserVisitDao();
	}

	@Override
	public boolean exist(long login) {
		String userLogin;
		try {
			userLogin = userDao.findUser(login);
			return userLogin != null && !userLogin.isEmpty();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void saveUser(GHUser ghUser) {
		try {

			userDao.addUser(ghUser);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void saveUserFollower(long user, long follower) {
		try {
			userNetworkDao.saveUserFollowing(user, follower);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}

	@Override
	public void saveUserToVisit(long userId, String userLogin) {
		try {
			userVisitDao.addToVisitLog(userId, userLogin);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String findOneUnvisitedUser() {
		try {
			return userVisitDao.getNextRandomly();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void doneVisitingUser(long login) {
		try {
			userVisitDao.markAsVisited(login);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
