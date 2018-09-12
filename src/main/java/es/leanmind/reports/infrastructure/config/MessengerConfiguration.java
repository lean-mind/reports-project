package es.leanmind.reports.infrastructure.config;

import es.leanmind.reports.domain.InvoiceService;
import es.leanmind.reports.infrastructure.Messenger;
import es.leanmind.reports.infrastructure.SimpleMessenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessengerConfiguration {

    private final InvoiceService invoiceService;

    @Autowired
    public MessengerConfiguration(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Bean
    public Messenger simpleMessenger() {
        return new SimpleMessenger(invoiceService);
    }

}
