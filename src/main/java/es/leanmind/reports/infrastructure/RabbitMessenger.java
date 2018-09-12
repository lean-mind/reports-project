package es.leanmind.reports.infrastructure;

import com.rabbitmq.client.*;

import java.io.IOException;

public class RabbitMessenger implements Messenger {
    private final String QUEUE_NAME = "ftp-queue";
    private Channel channel;
    private Connection connection;
    private final Consumer consumer = new DefaultConsumer(channel) {
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            String message = new String(body, "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        }
    };

    @Override
    public void init() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicConsume(QUEUE_NAME, true, consumer);
        }
        catch(Exception e){
            // TODO: log exception
            e.printStackTrace();
        }

    }

    @Override
    public void terminate() {
        try {
            channel.close();
            connection.close();
        }
        catch(Exception e){
            // TODO: log exception
            e.printStackTrace();
        }
    }

    @Override
    public void send(MessageType type, String... content) {
        try {
            String path = content[0];
            //TODO: send establishment
            // String establishment = content[1];
            channel.basicPublish("", QUEUE_NAME, null, path.getBytes());
        } catch (Exception e) {
            // TODO: log exception
            e.printStackTrace();
        }
    }
}
