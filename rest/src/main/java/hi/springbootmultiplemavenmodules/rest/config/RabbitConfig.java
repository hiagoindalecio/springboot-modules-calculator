package hi.springbootmultiplemavenmodules.rest.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import hi.springbootmultiplemavenmodules.library.constants.RabbitConstants;

@Configuration
@Component
public class RabbitConfig {

    private AmqpAdmin amqpAdminInstance;

    public RabbitConfig(AmqpAdmin pAmqpAdmin) {
        this.amqpAdminInstance = pAmqpAdmin;
    }

    private Queue queue(String pQueueName) {
        return new Queue(pQueueName);
    }

    private Binding binding(Queue pQueue, TopicExchange pExchange) {
        return new Binding(pQueue.getName(),
                            Binding.DestinationType.QUEUE,
                            pExchange.getName(),
                            pQueue.getName(),
                   null);
    }

    TopicExchange exchange() {
        return new TopicExchange(RabbitConstants.RPC_EXCHANGE);
    }

    @PostConstruct
    private void producer() {
        Queue vQueueMsg = this.queue(RabbitConstants.QUEUE_OP_MSG);
        Queue vQueueReply = this.queue(RabbitConstants.QUEUE_OP_REPLY);

        TopicExchange vExchange = this.exchange();

        Binding vBindingAddMsg = this.binding(vQueueMsg, vExchange);
        Binding vBindingAddReply = this.binding(vQueueReply, vExchange);

        this.amqpAdminInstance.declareQueue(vQueueMsg);
        this.amqpAdminInstance.declareQueue(vQueueReply);

        this.amqpAdminInstance.declareExchange(vExchange);

        this.amqpAdminInstance.declareBinding(vBindingAddMsg);
        this.amqpAdminInstance.declareBinding(vBindingAddReply);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory pConnectionFactory) {
        RabbitTemplate template = new RabbitTemplate(pConnectionFactory);
        template.setReplyTimeout(9000);
        template.setReplyAddress(RabbitConstants.QUEUE_OP_REPLY);
        return template;
    }

    @Bean
    SimpleMessageListenerContainer replyAddContainer(ConnectionFactory pConnectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(pConnectionFactory);
        container.setMessageListener(rabbitTemplate(pConnectionFactory));
        container.setQueueNames(RabbitConstants.QUEUE_OP_REPLY);
        return container;
    }

}
