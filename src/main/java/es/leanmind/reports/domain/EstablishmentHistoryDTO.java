package es.leanmind.reports.domain;

import java.util.Arrays;

public class EstablishmentHistoryDTO {

    public String name;
    public String[] grossTotals;
    public String[] netTotals;
    public String[] numberOfInvoices;

    @Override
    public String toString() {
        return "EstablishmentHistoryDTO{" +
                "name='" + name + '\'' +
                ", grossTotals=" + Arrays.toString(grossTotals) +
                '}';
        // In case it fails in tests it gives us readable information
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstablishmentHistoryDTO)) return false;

        EstablishmentHistoryDTO that = (EstablishmentHistoryDTO) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return  Arrays.equals(grossTotals, that.grossTotals);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(grossTotals);
        return result;
    }
}
