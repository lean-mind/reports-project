package es.leanmind.reports.infrastructure;

import es.leanmind.reports.domain.EstablishmentService;
import es.leanmind.reports.domain.FtpUser;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

@Component
public class CustomUserManager implements UserManager {

    @Value("${ftp.home-directory}")
    private String homeDirectory;
    @Value("${ftp.destination-folder}")
    private String destinationFolder;

    private EstablishmentService service;

    @Autowired
    public CustomUserManager(EstablishmentService service) {
        this.service = service;
    }

    @Override
    public User getUserByName(String name) {
        // We are returning a stub user so that the FTP server
        // can proceed to the actual authentication part...
        BaseUser x = new BaseUser();
        x.setName(name);
        giveAuthorityTo(x);
        return x;
    }

    private void giveAuthorityTo(BaseUser user) {
        ArrayList<Authority> authorities = new ArrayList<>();
        authorities.add(new WritePermission());
        authorities.add(new ConcurrentLoginPermission(100, 100));
        user.setAuthorities(authorities);
    }

    @Override
    public String[] getAllUserNames() throws FtpException {
        throw new NotImplementedException();
    }

    @Override
    public void delete(String userName) throws FtpException {
        throw new NotImplementedException();
    }

    @Override
    public void save(User user) throws FtpException {
        throw new NotImplementedException();
    }

    @Override
    public boolean doesExist(String userName) throws FtpException {
        throw new NotImplementedException();
    }

    @Override
    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
        UsernamePasswordAuthentication auth = (UsernamePasswordAuthentication) authentication;
        String username = auth.getUsername();
        String password = auth.getPassword();

        if (!service.isFtpUserAllowed(username, password)) {
            throw new AuthenticationFailedException();
        }

        FtpUser ftpUser = service.ftpUserWith(username);
        BaseUser user = new BaseUser();
        user.setName(username);
        giveAuthorityTo(user);

        user.setHomeDirectory(System.getProperty(homeDirectory) +
                            java.nio.file.FileSystems.getDefault().getSeparator() +
                            ftpUser.getHomeFolder());

        return user;
    }

    public String getPath(){
        return System.getProperty(homeDirectory)
                + java.nio.file.FileSystems.getDefault().getSeparator()
                + destinationFolder
                + java.nio.file.FileSystems.getDefault().getSeparator();
    }

    @Override
    public String getAdminName() throws FtpException {
        throw new NotImplementedException();
    }

    @Override
    public boolean isAdmin(String userName) throws FtpException {
        return false;
    }
}
