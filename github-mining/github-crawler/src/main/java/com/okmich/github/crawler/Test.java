/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okmich.github.crawler;

import java.io.IOException;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 *
 * @author michael.enudi
 */
public class Test {

    public static void main(String[] args) throws IOException {
        GitHub github = GitHub.connectAnonymously();

        GHRepository repo = github.getRepository("ueshin/apache-spark");
        System.out.println(repo);
        System.out.println(repo.getOwner());
        System.out.println(repo.getOwner().getFollowersCount());
        System.out.println(repo.getOwner().getFollowingCount());
        System.out.println(repo.listSubscribers().asList());
    }
}
