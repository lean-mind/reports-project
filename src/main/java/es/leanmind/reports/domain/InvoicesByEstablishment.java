package es.leanmind.reports.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class InvoicesByEstablishment {

    private Map<Integer, List<StoredInvoice>> items;

    public InvoicesByEstablishment(Map<Integer, List<StoredInvoice>> items) {
        this.items = items;
    }

    public StoredInvoice get(Integer establishmentId, int index) {
        return items.get(establishmentId).get(index);
    }

    public List<StoredInvoice> get(Integer establishmentId) {
        return new ArrayList(items.get(establishmentId));
    }

    public Stream<List<StoredInvoice>> byEstablishment() {
        return items.values().stream();
    }

    public Set<Integer> establishmentsIds() {
        return items.keySet();
    }

    public String establishmentName(Integer id){
        try {
            return items.get(id).get(0).establishmentName();
        }
        catch (Exception e){
            throw new RuntimeException("Establishment not found");
        }
    }
}
