package es.leanmind.reports.infrastructure;

import es.leanmind.reports.domain.InvoiceService;

public class SimpleMessenger implements Messenger {

    private InvoiceService invoiceService;

    public SimpleMessenger(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public void init() {}

    @Override
    public void terminate() {}

    @Override
    public void send(MessageType type, String... content) {
        if (type == MessageType.FtpFileUploaded){
            InvoiceService service = invoiceService;
            String path = content[0];
            String username = content[1];

            try {
                service.store(path, username);
            } catch (Exception e) {
                // TODO: log exception
                e.printStackTrace();
            }
        }
    }
}
