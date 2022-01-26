package net.xeill.elpuig;

import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HackerRankScraper {

    WebDriver driver;
    WebDriverWait wait;

    public HackerRankScraper() {
        System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver");
        FirefoxOptions options = new FirefoxOptions();

        driver = new FirefoxDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_TIMEOUT));
    }

    public void doLogin() {
        driver.get("https://www.hackerrank.com/auth/login");

        // Buscamos el elemento input-1 que contiene el input para el nombre de usuario
        WebElement inputName = driver.findElement(new By.ById("input-1"));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(new By.ById("input-1"))));
        inputName.sendKeys(Constants.HACKERRANK_USER);

        WebElement inputPassword = driver.findElement(new By.ById("input-2"));
        wait.until(ExpectedConditions.elementToBeClickable(inputPassword));
        inputPassword.sendKeys(Constants.HACKERRANK_PASSWORD);

        WebElement loginForm = driver.findElement(new By.ByClassName("login-form"));
        WebElement submitButton = loginForm.findElement(new By.ByTagName("button"));

        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
    }

    // A team could be an unique user
    public ArrayList<ArrayList<String>> getTeamSubmissionsData(String user) {
        driver.get("https://www.hackerrank.com/contests/" + Constants.HACKERRANK_CONTEST + "/judge/submissions/team/" + user);

        wait.until(ExpectedConditions.elementToBeClickable(new By.ByClassName("pagination")));
        WebElement pagination = driver.findElement(new By.ByClassName("pagination"));

        // Get the number of pages with submissions
        int pages = getNumberOfPages(pagination);
        System.out.println(pages);

        ArrayList<ArrayList<String>> teamSubmissionsDataLines = new ArrayList<ArrayList<String>>();

        for (int i = 1; i <= pages; i++) {
            for (ArrayList<String> submissionsLine : getTeamSubmissionsDataByPage(user, i)) {
                teamSubmissionsDataLines.add(submissionsLine);
            }
        }

        // ArrayList<Submission> teamSubmissions = getTeamSubmissionsFromData(teamSubmissionsDataLines);
        return teamSubmissionsDataLines;
    }

    public ArrayList<Submission> getTeamSubmissionsFromData(ArrayList<ArrayList<String>> teamSubmissionsData) {
        ArrayList<Submission> submissions = new ArrayList<Submission>();

        for (ArrayList<String> line : teamSubmissionsData) {
            submissions.add(line2Submission(line));
        }

        return submissions;
    }

    public List<ArrayList<String>> getTeamSubmissionsDataByPage(String user, int i) {

        // Problem  Team  ID  Language  Time  Result  Score  Status  During Contest?
        // Tres en ratlla #if  rusben  1338769123  java8  859517  Accepted  1  Yes  View
        // https://www.hackerrank.com/contests/dam-m3/challenges/c2-l1-5-tres-en-ratlla/submissions/code/1338769123

        driver.get("https://www.hackerrank.com/contests/" + Constants.HACKERRANK_CONTEST + "/judge/submissions/team/" + user + "/" + i);

        wait.until(ExpectedConditions.elementToBeClickable(new By.ByClassName("submissions_list")));
        WebElement submissions = driver.findElement(new By.ByClassName("submissions_list"));

        List<WebElement> submissionsRow = submissions.findElements(new By.ByClassName("judge-submissions-list-view"));
        ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();

        for (WebElement row : submissionsRow) {
            List<WebElement> cells = row.findElements(new By.ByClassName("submissions-title"));
            ArrayList<String> line = new ArrayList<String>();

            // Add the contest to the line
            System.out.println("------------------");
            System.out.println(Constants.HACKERRANK_CONTEST);
            line.add(Constants.HACKERRANK_CONTEST);

            // Add the challenge URL
            String challengeURL = row.findElement(new By.ByClassName("challenge-slug")).getAttribute("href");
            System.out.println(challengeURL);
            line.add(challengeURL);

            for (WebElement cell : cells) {
                System.out.println(cell.getText());
                line.add(cell.getText());
            }

            // Get the href to view the code
            WebElement viewResults = row.findElement(By.className("view-results"));

            System.out.println(viewResults.getAttribute("href"));
            line.add(viewResults.getAttribute("href"));

            System.out.println();

            // Add the line to the arraylist
            lines.add(line);
        }

        return lines;
    }

    public void csvWrite(List<List<String>> data, String path) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(path));

            for (List<String> line : data) {
                writer.writeNext((String[]) line.toArray());
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Submission line2Submission(ArrayList<String> line) {
        Submission submission = new Submission();

        submission.setContest(line.get(0));
        submission.setChallenge(new Challenge(line.get(1), line.get(2)));
        submission.setOwner(line.get(3));
        submission.setAtTime(Long.parseLong(line.get(4)));
        submission.setLanguage(line.get(5));
        submission.setId(Long.parseLong(line.get(6)));
        submission.setType(text2SubmissionType(line.get(7)));
        submission.setScore(Float.parseFloat(line.get(8)));
        submission.setDuringContest(text2Boolean(line.get(10)));
        submission.setURL(line.get(11));

        return submission;
    }

    public int getNumberOfPages(WebElement pagination) {
        // Retrieve the list of a with the pagination pages
        List<WebElement> paginationA = pagination.findElements(new By.ByTagName("a"));

        // get the attribute data-page from the last a href in pagination
        return Integer.parseInt(paginationA.get(paginationA.size() - 1).getAttribute("data-page"));
    }

    public boolean text2Boolean(String text) {
        boolean result = false;

        switch (text) {
            case "Yes":
                result = true;
                break;
            case "No":
                result = false;
                break;
            default:
                break;
        }

        return result;
    }

    public SubmissionType text2SubmissionType(String text) {
        SubmissionType submissionType = null;

        switch (text) {
            case "Wrong Answer":
                submissionType = SubmissionType.WRONG_ANSWER;
                break;
            case "Accepted":
                submissionType = SubmissionType.ACCEPTED;
                break;
            case "Terminated due to timeout":
                submissionType = SubmissionType.TERMINATED_DUE_TO_TIMEOUT;
                break;
            case "Runtime error":
                submissionType = SubmissionType.RUNTIME_ERROR;
                break;
            case "Segmentation fault":
                submissionType = SubmissionType.SEGMENTATION_FAULT;
                break;
            case "Abort called":
                submissionType = SubmissionType.ACCEPTED;
                break;
            default:
                break;
        }

        return submissionType;
    }

    public ArrayList<String> getTeams() {
        try {

            BufferedReader inputStream = new BufferedReader(new FileReader(new File("src/main/resources/hackerrankers.txt")));

            String line = "";
            ArrayList<String> names = new ArrayList<String>();

            while ((line = inputStream.readLine()) != null) {
                names.add(line);
            }

            inputStream.close();

            return names;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}




