package net.xeill.elpuig;

public class Submission {
    private String contest;
    private Challenge challenge;
    private String owner;
    private long atTime;
    private String language;
    private long id;
    private SubmissionType type;
    private float score;
    private boolean duringContest;
    private String URL;

    public String getContest() {
        return contest;
    }

    public void setContest(String contest) {
        this.contest = contest;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public long getAtTime() {
        return atTime;
    }

    public void setAtTime(long atTime) {
        this.atTime = atTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SubmissionType getType() {
        return type;
    }

    public void setType(SubmissionType type) {
        this.type = type;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isDuringContest() {
        return duringContest;
    }

    public void setDuringContest(boolean duringContest) {
        this.duringContest = duringContest;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "contest='" + contest + '\'' +
                ", challenge=" + challenge +
                ", owner='" + owner + '\'' +
                ", atTime=" + atTime +
                ", language='" + language + '\'' +
                ", id=" + id +
                ", type=" + type +
                ", score=" + score +
                ", duringContest=" + duringContest +
                ", URL='" + URL + '\'' +
                '}';
    }
}
