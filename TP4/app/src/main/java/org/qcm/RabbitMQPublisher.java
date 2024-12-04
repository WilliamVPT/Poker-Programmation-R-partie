package main.java.org.qcm;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

public class RabbitMQPublisher {
    private static final String QUEUE_NAME = "qcm_queue";

    public static void publishQCM(String jsonQCM) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.11.18.44"); // Changez si RabbitMQ est sur une autre machine
        factory.setUsername("verp0005");  // Assurez-vous que c'est le bon nom d'utilisateur
        factory.setPassword("oui");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, jsonQCM.getBytes());
            System.out.println("Published QCM to RabbitMQ: " + jsonQCM);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
