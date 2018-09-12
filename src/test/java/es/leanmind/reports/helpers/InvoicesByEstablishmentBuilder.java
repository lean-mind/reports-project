package es.leanmind.reports.helpers;

import es.leanmind.reports.domain.InvoicesByEstablishment;
import es.leanmind.reports.domain.StoredInvoice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class InvoicesByEstablishmentBuilder {
    Map<Integer, List<StoredInvoice>> items = new HashMap<>();

    public InvoicesByEstablishmentBuilder with(int id, StoredInvoice... invoices){
        items.put(id, asList(invoices));
        return this;
    }

    public InvoicesByEstablishment areGiven(){
        return new InvoicesByEstablishment(items);
    }
}
