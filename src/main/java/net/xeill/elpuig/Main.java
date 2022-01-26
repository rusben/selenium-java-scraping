package net.xeill.elpuig;

import java.util.ArrayList;

public class Main {

  public static void main(String[] args) {
    HackerRankScraper hrs = new HackerRankScraper();
    hrs.doLogin();

    ArrayList<ArrayList<String>> submissionsData =  hrs.getTeamSubmissionsData("rusben");
    ArrayList<Submission> submissions = hrs.getTeamSubmissionsFromData(submissionsData);

    for (Submission submission : submissions) {
      System.out.println(submission);
    }

  }

}
