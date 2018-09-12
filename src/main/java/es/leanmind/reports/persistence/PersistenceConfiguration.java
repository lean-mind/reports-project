package es.leanmind.reports.persistence;

import es.leanmind.reports.domain.InvoiceRepository;
import es.leanmind.reports.domain.EstablishmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PersistenceConfiguration {

    @Value("${spring.datasource.url}")
    private String connectionUrl;

    @Bean
    @Primary
    public InvoiceRepository postgreInvoiceRepository() {
        return new PostgresInvoiceRepository(connectionUrl);
    }

    @Bean
    public EstablishmentRepository establishmentRepository(){
        return new PostgresEstablishmentRepository(connectionUrl);
    }

}
