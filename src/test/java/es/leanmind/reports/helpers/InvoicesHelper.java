package es.leanmind.reports.helpers;

import es.leanmind.reports.domain.StoredInvoice;

import java.math.BigDecimal;

public class InvoicesHelper {

    public static int irrelevantInvoiceId;
    public static String midnight = "T00:00:00";
    public static String beforeMidnight = "T23:00:00";
    public static String october2 = "2016-10-02";
    public static String october3 = "2016-10-03";
    public static String march2 = "2016-03-02";
    public static String march3 = "2016-03-03";
    public static String february1 = "2016-02-01";
    public static String january3 = "2016-01-03";
    public static String january2 = "2016-01-02";
    public static String january1 = "2016-01-01";
    public static String april30_2016 = "2016-04-30";
    public static String april1_2016 = "2016-04-01";
    public static String sunday_april3_2016 = "2016-04-03";
    public static String march1_2016 = "2016-03-01";
    public static String march2_2016 = "2016-03-02";
    public static String may30 = "2016-05-30";
    public static String monday_may2_2016 = "2016-05-02";
    public static String march22 = "2016-03-22";
    public static String february2 = "2016-02-02";
    public static String may1 = "2016-05-01";

    public static InvoicesByEstablishmentBuilder invoicesByEstablishment(){
        return new InvoicesByEstablishmentBuilder();
    }

    public static StoredInvoice aStoredInvoice(int establishmentId, String fromDay, String grossAmount){
        return aStoredInvoice(establishmentId, fromDay, withAnyTime(fromDay), grossAmount, "0");
    }

    public static StoredInvoice aStoredInvoice(int establishmentId, String fromDay, String fromDate, String grossAmount){
        return aStoredInvoice(establishmentId, fromDay, fromDate, grossAmount, "0");
    }

    public static StoredInvoice aStoredInvoice(int establishmentId, String fromDay, String fromDate, String grossAmount, String netAmount){
        irrelevantInvoiceId++;
        return new StoredInvoice(irrelevantInvoiceId, establishmentId, "establishment_" + establishmentId,
                fromDay, fromDate, new BigDecimal(grossAmount), new BigDecimal(netAmount));
    }

    public static String withAnyTime(String date){
        return date + midnight;
    }

    public static String withTime(String date, String hours, String minutes){
        return date + "T" + hours + ":" + minutes + ":00";
    }
}
