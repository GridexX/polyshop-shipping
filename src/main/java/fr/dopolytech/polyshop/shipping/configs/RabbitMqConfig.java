package fr.dopolytech.polyshop.shipping.configs;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.dopolytech.polyshop.shipping.services.ShippingService;


@Configuration
public class RabbitMqConfig {

  // Listen for the payment service to send shipping messages
  @Bean
  public Queue shippingQueue() {
    return new Queue("shipping", false);
  }

  @Bean
  public Queue shippingCancelQueue() {
    return new Queue("shipping_cancel", false);
  }

  @Bean
  public Queue inventoryConfirmedQueue() {
    return new Queue("inventory_confirmed", false);
  }

  @Bean
  public MessageListenerAdapter listenerAdapter(ShippingService shippingService) {
    return new MessageListenerAdapter(shippingService, "receiveMessage");
  }

  @Bean
  public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames("shipping");
    container.setMessageListener(listenerAdapter);
    return container;
  }
}