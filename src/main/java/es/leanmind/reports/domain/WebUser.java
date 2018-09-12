package es.leanmind.reports.domain;

public class WebUser {

    private final String username;
    private final String passwordHash;

    public WebUser(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
