package es.leanmind.reports.domain;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstablishmentService {

    private final EstablishmentRepository establishmentRepository;

    @Autowired
    public EstablishmentService(EstablishmentRepository establishmentRepository) {
        this.establishmentRepository = establishmentRepository;
    }

    public void create(String name, Credentials ftpCredentials, String ftpHomeFolder, Credentials webCredentials) {
        String hashedFtpPassword = BCrypt.hashpw(ftpCredentials.password, BCrypt.gensalt(12));
        String hashedWebPassword = BCrypt.hashpw(webCredentials.password, BCrypt.gensalt(12));
        FtpUser ftpUser = new FtpUser(ftpCredentials.username, hashedFtpPassword, ftpHomeFolder);
        WebUser webUser = new WebUser(webCredentials.username, hashedWebPassword);
        Establishment establishment = new Establishment(name, ftpUser, webUser);
        establishmentRepository.create(establishment);
    }

    public List<EstablishmentDTO> retrieveEstablishmentsFor(String username) {
        return establishmentRepository.retrieveEstablishmentsFor(username);
    }

    public boolean isFtpUserAllowed(String username, String password) {
        String retrievedPasswordHash = establishmentRepository.getFtpUserHashedPassword(username);
        return BCrypt.checkpw(password, retrievedPasswordHash);
    }

    public boolean isWebUserAllowed(String username, String password) {
        String retrievedPasswordHash = establishmentRepository.getWebUserHashedPassword(username);
        return BCrypt.checkpw(password, retrievedPasswordHash);
    }

    public FtpUser ftpUserWith(String username) {
        return establishmentRepository.retrieveFtpUser(username);
    }
}
