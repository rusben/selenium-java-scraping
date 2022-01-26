package net.xeill.elpuig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Constants {

    public static final String HACKERRANK_USER;
    public static final String HACKERRANK_PASSWORD;
    public static final String HACKERRANK_CONTEST;
    public static final int DEFAULT_TIMEOUT;

    static {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("src/main/constants.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HACKERRANK_USER = p.getProperty("HACKERRANK_USER");
        HACKERRANK_PASSWORD = p.getProperty("HACKERRANK_PASSWORD");
        HACKERRANK_CONTEST = p.getProperty("HACKERRANK_CONTEST");
        DEFAULT_TIMEOUT = Integer.parseInt(p.getProperty("DEFAULT_TIMEOUT"));
    }
}