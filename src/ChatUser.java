/**
 *
 *  @author Michalski Hubert S28546
 *
 */


import javax.jms.*;
import javax.naming.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatUser {
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private TopicSubscriber subscriber;
    private String nickname;

    public ChatUser(String nickname, MessageListener listener) {
        this.nickname = nickname;

        try {
            Context ctx = new InitialContext();
            ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
            Topic topic = (Topic) ctx.lookup("topic1");

            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(topic);

            subscriber = session.createDurableSubscriber(topic, nickname);
            subscriber.setMessageListener(listener);
            
            connection.start();

            sendMessage("joined the chat.");
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
            close();
        }
    }

    public void sendMessage(String message) throws JMSException {
        TextMessage textMessage = session.createTextMessage();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM HH:mm"));
        textMessage.setText("[" + timestamp + "] " + nickname + ": " + message);
        producer.send(textMessage);
    }

    public void close() {
        try {
        	sendMessage("left the chat.");
            subscriber.close();
            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
        	e.printStackTrace();
        }
    }
}
