package kled.chen.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

@Configuration
public class ActiveMQConfig {
    @Bean
    public JmsListenerContainerFactory<?> topicListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(true);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> p2pListenerFactoryWithJsonConverter(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setMessageConverter(jackson2MessageConverter());
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    //注: Spring容器启动时并不会直接向Jms Provider注册Queue和Topic，只有在@JmsListener中申明或者向Jms Provider发送消息时才会注册
    //如果Queue和Topic不存在时, 生产者会根据Destination的类型注册, 消费者监听时则取决于ListenerContainerFactory的模式(P2P或者Topic)注册Destination
    @Bean
    public Queue myQueue(){
        return new ActiveMQQueue("myQueue");
    }

    @Bean
    public Queue myRpcQueue(){
        return new ActiveMQQueue("myRpcQueue");
    }

    @Bean
    public Queue mySerializableQueue(){
        return new ActiveMQQueue("mySerializableQueue");
    }

    @Bean
    public Queue myJacksonQueue(){
        return new ActiveMQQueue("myJacksonQueue");
    }

    @Bean
    public Topic myTopic(){
        return new ActiveMQTopic("myTopic");
    }

    //注：此处用JmsMessagingTemplate配置jmsMessageConverter会异常
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory){
        JmsTemplate jacksonJmsTemplate = new JmsTemplate();
        jacksonJmsTemplate.setConnectionFactory(connectionFactory);
        jacksonJmsTemplate.setMessageConverter(jackson2MessageConverter());
        return jacksonJmsTemplate;
    }

    @Bean
    public MappingJackson2MessageConverter jackson2MessageConverter(){
        MappingJackson2MessageConverter messageConverter =
                new MappingJackson2MessageConverter();
        messageConverter.setTargetType(MessageType.TEXT);
        messageConverter.setTypeIdPropertyName("_type");
        return messageConverter;
    }
}
