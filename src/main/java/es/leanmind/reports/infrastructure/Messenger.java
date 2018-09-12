package es.leanmind.reports.infrastructure;

import org.springframework.stereotype.Component;

@Component
public interface Messenger {
    void init();
    void terminate();
    void send(MessageType type, String... content);
}

