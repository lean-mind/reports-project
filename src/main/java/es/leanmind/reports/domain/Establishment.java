package es.leanmind.reports.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Establishment {

    private String name;
    private FtpUser ftpUser;
    private Set<WebUser> webUsers;

    public Establishment(String name, FtpUser ftpUser, WebUser... webUsers) {
        this.name = name;
        this.ftpUser = ftpUser;
        this.webUsers = new HashSet<>(Arrays.asList(webUsers));
    }

    public String name() {
        return name;
    }

    public FtpUser ftpUser() {
        return ftpUser;
    }

    public Set<WebUser> webUsers() {
        return new HashSet<>(webUsers);
    }
}
