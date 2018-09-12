package es.leanmind.reports.domain;

import java.math.BigDecimal;

public class UploadedInvoice {

    private final String businessDay;
    private final String date;
    private final String grossAmount;
    private final String netAmount;

    public UploadedInvoice(String businessDay, String date, String grossAmount, String netAmount) {
        this.businessDay = businessDay;
        this.date = date;
        this.grossAmount = grossAmount;
        this.netAmount = netAmount;
    }

    public String businessDay() {
        return businessDay;
    }

    public String date() {
        return date;
    }

    public BigDecimal grossAmount() {
        return new BigDecimal(grossAmount);
    }

    public BigDecimal netAmount() {
        return new BigDecimal(netAmount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UploadedInvoice that = (UploadedInvoice) o;

        if (businessDay != null ? !businessDay.equals(that.businessDay) : that.businessDay != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (grossAmount != null ? !grossAmount.equals(that.grossAmount) : that.grossAmount != null) return false;
        return netAmount != null ? netAmount.equals(that.netAmount) : that.netAmount == null;
    }

    @Override
    public int hashCode() {
        int result = businessDay != null ? businessDay.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (grossAmount != null ? grossAmount.hashCode() : 0);
        result = 31 * result + (netAmount != null ? netAmount.hashCode() : 0);
        return result;
    }
}
