package es.leanmind.reports.domain;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstablishmentRepository {

    void create(Establishment establishment);

    List<EstablishmentDTO> retrieveEstablishmentsFor(String username);

    String getWebUserHashedPassword(String username);

    String getFtpUserHashedPassword(String username);

    FtpUser retrieveFtpUser(String username);
}
