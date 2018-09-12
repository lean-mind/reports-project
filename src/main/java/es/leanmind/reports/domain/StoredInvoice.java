package es.leanmind.reports.domain;

import java.math.BigDecimal;

public class StoredInvoice {

    private Integer id;
    private Integer establishmentId;
    private String establishmentName;
    private final String businessDay;
    private final String date;
    private final BigDecimal grossAmount;
    private final BigDecimal netAmount;

    public StoredInvoice(Integer id, Integer establishmentId, String establishmentName, String businessDay, String date, BigDecimal grossAmount, BigDecimal netAmount) {
        this.id = id;
        this.establishmentId = establishmentId;
        this.establishmentName = establishmentName;
        this.businessDay = businessDay;
        this.date = date;
        this.grossAmount = grossAmount;
        this.netAmount = netAmount;
    }

    public Integer establishmentId() {
        return establishmentId;
    }

    public String establishmentName() {
        return establishmentName;
    }

    public String businessDay() {
        return businessDay;
    }

    public String date() {
        return date;
    }

    public BigDecimal grossAmount() { return grossAmount; }

    public BigDecimal netAmount() { return netAmount; }

    public Integer getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoredInvoice invoice = (StoredInvoice) o;

        if (establishmentId != null ? !establishmentId.equals(invoice.establishmentId) : invoice.establishmentId != null)
            return false;
        if (establishmentName != null ? !establishmentName.equals(invoice.establishmentName) : invoice.establishmentName != null)
            return false;
        if (businessDay != null ? !businessDay.equals(invoice.businessDay) : invoice.businessDay != null) return false;
        if (date != null ? !date.equals(invoice.date) : invoice.date != null) return false;
        if (grossAmount != null ? !grossAmount.equals(invoice.grossAmount) : invoice.grossAmount != null) return false;
        return netAmount != null ? netAmount.equals(invoice.netAmount) : invoice.netAmount == null;
    }

    @Override
    public int hashCode() {
        int result = establishmentId != null ? establishmentId.hashCode() : 0;
        result = 31 * result + (establishmentName != null ? establishmentName.hashCode() : 0);
        result = 31 * result + (businessDay != null ? businessDay.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (grossAmount != null ? grossAmount.hashCode() : 0);
        result = 31 * result + (netAmount != null ? netAmount.hashCode() : 0);
        return result;
    }
}
