package es.leanmind.reports.persistence;

import es.leanmind.reports.domain.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PostgresInvoiceRepository implements InvoiceRepository {

    private Sql2o sql2o;

    public PostgresInvoiceRepository(String connectionUrl) {
        sql2o = new Sql2o(connectionUrl, "reports", "12345");
    }

    @Override
    public void save(UploadedInvoices invoices) {
        Integer establishmentId = establishmentIdFromFtpusername(invoices.ftpUsername());
        String query = "INSERT INTO invoices(establishmentId, businessDay, date, grossAmount, netAmount) " +
                       "VALUES (:establishmentId, :businessDay, :date, :grossAmount, :netAmount)";

        invoices.getItems().forEach(invoice -> {
            try(Connection connection = sql2o.open()) {
                connection.createQuery(query)
                        .addParameter("establishmentId", establishmentId)
                        .addParameter("businessDay", invoice.businessDay())
                        .addParameter("date", invoice.date())
                        .addParameter("grossAmount", invoice.grossAmount().toString())
                        .addParameter("netAmount", invoice.netAmount().toString())
                        .executeUpdate();
            }
        });
    }

    private Integer establishmentIdFromFtpusername(String ftpUsername) {
        String establishmentQuery = "SELECT establishment FROM ftpusers " +
                                    "WHERE username = :ftpusername";
        try(Connection connection = sql2o.open()) {
            return (Integer) connection.createQuery(establishmentQuery)
                    .addParameter("ftpusername", ftpUsername)
                    .executeScalar();
        }
    }

    @Override
    public InvoicesByEstablishment retrieveStoredInvoices(ReportFilters filters) {
        String query =
                "SELECT * FROM INVOICES " +
                "INNER JOIN establishments ON establishments.id = invoices.establishmentId " +
                "WHERE establishmentId = ANY (string_to_array(:establishments, ',')::INT[])";
        try(Connection connection = sql2o.open()) {
            List<Map<String, Object>> queryResults = connection
                    .createQuery(query)
                    .addParameter("establishments", StringUtils.join(filters.establishments(), ", "))
                    .executeAndFetchTable().asList();

            Map<Integer, List<StoredInvoice>> items = queryResults.stream()
                    .map(this::toStoredInvoice)
                    .filter(invoice -> filters.haveDateRangeContaining(invoice.businessDay()))
                    .collect(Collectors.groupingBy(StoredInvoice::establishmentId));
            return new InvoicesByEstablishment(items);
        }
    }

    private StoredInvoice toStoredInvoice(Map<String, Object> result) {
        return new StoredInvoice(
            (Integer)result.get("id"),
            (Integer)result.get("establishmentid"),
            (String) result.get("name"),
            (String) result.get("businessday"),
            (String) result.get("date"),
            new BigDecimal((String) result.get("grossamount")),
            new BigDecimal((String) result.get("netamount"))
        );
    }

}
