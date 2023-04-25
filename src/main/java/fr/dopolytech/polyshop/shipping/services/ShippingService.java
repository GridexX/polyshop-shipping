package fr.dopolytech.polyshop.shipping.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class ShippingService {

  private final RabbitTemplate rabbitTemplate;
  private final Queue shippingQueue;

  // Define the logger
  private static final Logger logger = LoggerFactory.getLogger(ShippingService.class);

  @Autowired
  public ShippingService(RabbitTemplate rabbitTemplate, Queue shippingQueue) {
    this.rabbitTemplate = rabbitTemplate;
    this.shippingQueue = shippingQueue;
  }

  public void receiveMessage(String message) {
    logger.info("Received message from shipping queue: " + message);
  }
}