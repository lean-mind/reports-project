package es.leanmind.reports.infrastructure.config.ftp;

import es.leanmind.reports.controller.api.CustomFtplet;
import es.leanmind.reports.infrastructure.Messenger;
import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Configuration
public class FtpServerConfiguration {

    @Value("${ftp.port}")
    private Integer ftpPort;

    private final Messenger simpleMessenger;
    private UserManager customUserManager;

    @Autowired
    public FtpServerConfiguration(Messenger simpleMessenger, UserManager customUserManager) {
        this.simpleMessenger = simpleMessenger;
        this.customUserManager = customUserManager;
    }

    @PostConstruct
    public void startUpFtpServer(){
        FtpServerFactory serverFactory = new FtpServerFactory();
        ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
        connectionConfigFactory.setAnonymousLoginEnabled(false);

        serverFactory.setConnectionConfig(connectionConfigFactory.createConnectionConfig());
        serverFactory.setUserManager(customUserManager);

        HashMap<String, Ftplet> ftplets = new HashMap<>();

        ftplets.put("custom-ftplet", new CustomFtplet(simpleMessenger));

        ListenerFactory listenerFactory = new ListenerFactory();
        serverFactory.setFtplets(ftplets);
        listenerFactory.setPort(ftpPort);
        serverFactory.addListener("default", listenerFactory.createListener());
        FtpServer server = serverFactory.createServer();
        try {
            server.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }
}
