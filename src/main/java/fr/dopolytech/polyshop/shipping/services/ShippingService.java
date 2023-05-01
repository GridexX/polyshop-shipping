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

import fr.dopolytech.polyshop.shipping.messages.ErrorMessage;
import fr.dopolytech.polyshop.shipping.messages.MessageConfirmed;
import fr.dopolytech.polyshop.shipping.messages.ShippingMessage;

@Component
public class ShippingService {

  private final RabbitTemplate rabbitTemplate;
  private final Queue shippingQueue;
  private final Queue shippingCancelQueue;
  private final Queue inventoryConfirmedQueue;

  // Define the logger
  private static final Logger logger = LoggerFactory.getLogger(ShippingService.class);

  @Autowired
  public ShippingService(RabbitTemplate rabbitTemplate, Queue shippingQueue, Queue shippingCancelQueue,
      Queue inventoryConfirmedQueue) {
    this.rabbitTemplate = rabbitTemplate;
    this.shippingQueue = shippingQueue;
    this.shippingCancelQueue = shippingCancelQueue;
    this.inventoryConfirmedQueue = inventoryConfirmedQueue;
  }

  public void receiveMessage(String message) {
    logger.info("Received message from shipping (payement service) queue: " + message);
    try {
      ObjectMapper mapper = new ObjectMapper();

      ShippingMessage shippingMessage = mapper.readValue(message, ShippingMessage.class);

      // Randomly failed shipping
      if (Math.random() < 0.3) {
        ErrorMessage errorMessage = new ErrorMessage("failed", "Shipping failed", shippingMessage.orderId,
            shippingMessage.products);
        String errorMessageString = mapper.writeValueAsString(errorMessage);
        logger.info("Shipping failed. Sending message to shipping_cancel queue {}", errorMessageString);
        rabbitTemplate.convertAndSend(shippingCancelQueue.getName(), errorMessageString);
        return;
      }

      MessageConfirmed shippingMessageConfirmed = new MessageConfirmed(shippingMessage.orderId);
      String shippingMessageConfirmedString = mapper.writeValueAsString(shippingMessageConfirmed);
      logger.info("Shipping confirmed. Sending message to inventory_confirmed queue {}",
          shippingMessageConfirmedString);
      rabbitTemplate.convertAndSend(inventoryConfirmedQueue.getName(), shippingMessageConfirmedString);

    } catch (Exception e) {
      logger.error("Error while receiving message from shipping (payment service) queue: " + e.getMessage());
    }
  }
}