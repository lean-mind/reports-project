package es.leanmind.reports.helpers;

public class Properties {
    public final static String WEB_SERVER_PORT = "18080";
    public final static String FTP_SERVER_PORT = "10021";

//  Real system properties should not be used when testing since
//  conflicts with the real application or system resources may occur.
    public final static String SYSTEM_PROPERTY = "neverUse.realSystemProperty";

    public final static String testFtpFolderName = "test_workspace";
    public final static String testFtpProcessedFilesFolderName = "test_processed";
}
