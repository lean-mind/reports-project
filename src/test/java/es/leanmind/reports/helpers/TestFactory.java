package es.leanmind.reports.helpers;

import es.leanmind.reports.domain.EstablishmentService;
import es.leanmind.reports.persistence.PostgresEstablishmentRepository;

public class TestFactory {

    public static EstablishmentService establishmentService(String connection) {
        return new EstablishmentService(new PostgresEstablishmentRepository(connection));
    }
}
