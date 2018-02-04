/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler;

import java.util.HashMap;
import java.util.Map;

import org.kohsuke.github.GHUser;

import com.okmich.github.crawler.tasks.UserNetworkVisitorTask;
import com.okmich.github.crawler.tasks.db.HBaseUserDBImpl;
import com.okmich.github.crawler.tasks.db.JDBCUserDBImpl;
import com.okmich.github.crawler.tasks.db.MockUserDBImpl;
import com.okmich.github.crawler.tasks.db.UserDB;

/**
 *
 * @author michael.enudi
 */
public class Main {

	@SuppressWarnings("unused")
	private UserDB userDB;
	private GithubUserNetworkVisitorService crawlingServiceExecutor;

	private Main() {
	}

	public static void main(String[] args) {
		// -refuser=okmich -dbtype=mock
		Map<String, String> arguments = parseCmdLingArg(args);

		//
		Main main = new Main();
		UserDB _userDB = null;

		switch (arguments.get("dbtype")) {
		case "mysql":
			_userDB = new JDBCUserDBImpl();
			break;
		case "hbase":
			_userDB = new HBaseUserDBImpl();
			break;
		default:
			_userDB = new MockUserDBImpl();
		}

		main.userDB = _userDB;

		// initial user
		String refUser = arguments.get("refuser");
		if (arguments.containsKey("refuser"))
			processInitial(_userDB, refUser);

		main.crawlingServiceExecutor = new GithubUserNetworkVisitorService(
				_userDB);
		// start the crawler
		main.crawlingServiceExecutor.start();
	}

	private static Map<String, String> parseCmdLingArg(String args[]) {
		Map<String, String> hashMap = new HashMap<>(args.length);
		for (String arg : args) {
			String[] parts = arg.split("=");
			hashMap.put(parts[0].substring(1), parts[1]);
		}
		System.out.println("parameters: " + hashMap);
		return hashMap;
	}

	private static void processInitial(UserDB _userDB, String refUser) {
		System.out.println("Processing Initial " + refUser);
		// look up this user from github
		GHUser ghUser = new UserNetworkVisitorTask(null).getGHUser(refUser);
		// write the userId, and login name to visitlog
		_userDB.saveUserToVisit(ghUser.getId(), ghUser.getLogin());
	}
}
