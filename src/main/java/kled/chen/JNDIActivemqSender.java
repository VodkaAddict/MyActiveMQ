package kled.chen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Component
public class JNDIActivemqSender {

    private ConnectionFactory connectionFactory;

    @Autowired
    private MappingJackson2MessageConverter mappingJackson2MessageConverter;

    @PostConstruct
    public void init() throws NamingException {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://172.16.5.56:61616");
        Context ctx = new InitialContext(props);
        connectionFactory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
    }

    public void produceMsg(String msg, Destination destination) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        MessageProducer messageProducer = session.createProducer(destination);
        messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        Message message = mappingJackson2MessageConverter.toMessage("send msg:"+ msg + " from jndi connection factory", session);
        messageProducer.send(message);
        session.commit();
    }
}
