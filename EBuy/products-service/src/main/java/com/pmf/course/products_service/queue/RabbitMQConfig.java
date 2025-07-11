package com.pmf.course.products_service.queue;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE_NAME = "x.order-events";
    public static final String ORDER_QUEUE_NAME = "q.order-events";
    public static final String STOCK_EXCHANGE_NAME = "x.stock-events";
    public static final String STOCK_REJECTION_QUEUE_NAME = "q.stock-rejection";

    @Bean
    public Declarables rabbitBindings() {
        Queue orderQueue = new Queue(ORDER_QUEUE_NAME, true);
        Queue rejectionQueue = new Queue(STOCK_REJECTION_QUEUE_NAME, true);

        DirectExchange orderExchange = new DirectExchange(ORDER_EXCHANGE_NAME);
        DirectExchange stockExchange = new DirectExchange(STOCK_EXCHANGE_NAME);

        Binding orderBinding = BindingBuilder.bind(orderQueue)
                .to(orderExchange)
                .withQueueName();

        Binding stockRejectionBinding = BindingBuilder.bind(rejectionQueue)
                .to(stockExchange)
                .with("stock.rejected");

        return new Declarables(
                orderQueue,
                rejectionQueue,
                orderExchange,
                stockExchange,
                orderBinding,
                stockRejectionBinding
        );
    }

    @Bean
    public Jackson2JsonMessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter jsonConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonConverter);
        return template;
    }
}

