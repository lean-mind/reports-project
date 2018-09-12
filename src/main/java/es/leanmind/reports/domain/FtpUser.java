package es.leanmind.reports.domain;

public class FtpUser {

    private String username;
    private String passwordHash;
    private String homeFolder;

    public FtpUser(String username, String passwordHash, String homeFolder) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.homeFolder = homeFolder;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getHomeFolder() {
        return homeFolder;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
