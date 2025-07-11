package com.pmf.course.order_service.queue;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
    public Declarables topicBindings() {
        var orderQueue = new Queue(ORDER_QUEUE_NAME, true);
        var rejectionQueue = new Queue(STOCK_REJECTION_QUEUE_NAME, true);

        var orderExchange = new DirectExchange(ORDER_EXCHANGE_NAME, true, false);
        var stockExchange = new DirectExchange(STOCK_EXCHANGE_NAME, true, false);

        return new Declarables(
                orderQueue,
                rejectionQueue,
                orderExchange,
                stockExchange,
                BindingBuilder
                        .bind(orderQueue)
                        .to(orderExchange)
                        .withQueueName(),
                BindingBuilder
                        .bind(rejectionQueue)
                        .to(stockExchange)
                        .with("stock.rejected")
        );
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory, Jackson2JsonMessageConverter jsonConverter) {
        var template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonConverter);
        return template;
    }
}