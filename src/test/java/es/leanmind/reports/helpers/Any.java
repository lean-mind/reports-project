package es.leanmind.reports.helpers;

import java.util.UUID;

public class Any {

    public static String string() {
        return UUID.randomUUID().toString();
    }
}
