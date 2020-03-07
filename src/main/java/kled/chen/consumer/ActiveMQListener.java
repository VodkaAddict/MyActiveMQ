package kled.chen.consumer;

import kled.chen.form.HelloPlainRequest;
import kled.chen.form.HelloRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;


@Component
public class ActiveMQListener {

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "myQueue")
    public void listener1(String msg){
        System.out.println("点到点消费:listener1 receive msg=" + msg);
    }

    @JmsListener(destination = "mySerializableQueue")
    public void listener2(HelloRequest msg){
        System.out.println("点到点消费:listener2 receive serializable msg=" + msg);
    }

    @JmsListener(destination = "myJacksonQueue", containerFactory = "p2pListenerFactoryWithJsonConverter")
    public void listener3(HelloPlainRequest msg){
        System.out.println("点到点消费:listener3 receive json msg=" + msg);
    }

    @JmsListener(destination = "myRpcQueue", containerFactory = "p2pListenerFactoryWithJsonConverter")
    public void listener4(TextMessage message) throws JMSException {
        System.out.println("点到点消费:listener4 receive rpc msg=" + message.getText() + ", reply queue=" + message.getJMSReplyTo());
        jmsTemplate.convertAndSend(message.getJMSReplyTo(), "rpc response to msg:" + message.getText());
    }

    @JmsListener(destination = "myTopic", containerFactory = "topicListenerFactory")
    public void listener5(String msg){
        System.out.println("发布订阅消费:listener5 receive msg=" + msg);
    }

    @JmsListener(destination = "myTopic", containerFactory = "topicListenerFactory")
    public void listener6(String msg){
        System.out.println("发布订阅消费:listener6 receive msg=" + msg);
    }

}
