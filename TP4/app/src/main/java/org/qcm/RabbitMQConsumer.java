package main.java.org.qcm;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class RabbitMQConsumer {
    private static final String QUEUE_NAME = "qcm_queue";

    public static void consumeQCM() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody());
                System.out.println("Received QCM: " + message);
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
