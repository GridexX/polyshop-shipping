package fr.dopolytech.polyshop.shipping.messages;

public class ShippingMessage {

  public String id;
  public long orderId;
  public OrderStatus status;
  public List<ProductItem> products;

}