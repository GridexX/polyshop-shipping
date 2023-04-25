package fr.dopolytech.polyshop.shipping;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShippingApplication {

  private static final Logger logger = LoggerFactory.getLogger(ShippingApplication.class);

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(ShippingApplication.class, args);

    logger.info("Waiting for messages from shipping queue...");

    // Wait indefinitely for messages to be received
    synchronized (ShippingApplication.class) {
      ShippingApplication.class.wait();
    }
  }
}