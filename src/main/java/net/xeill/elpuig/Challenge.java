package net.xeill.elpuig;

public class Challenge {
    private String URL;
    private String title;

    Challenge(String URL, String title) {
        this.URL = URL;
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
