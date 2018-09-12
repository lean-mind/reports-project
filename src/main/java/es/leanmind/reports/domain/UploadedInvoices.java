package es.leanmind.reports.domain;

import java.util.List;

public class UploadedInvoices {

    private List<UploadedInvoice> items;
    private String ftpUsername;

    public UploadedInvoices(List<UploadedInvoice> items, String ftpUsername) {
        this.items = items;
        this.ftpUsername = ftpUsername;
    }

    public String ftpUsername() {
        return ftpUsername;
    }

    public List<UploadedInvoice> getItems() {
        return items;
    }
}
