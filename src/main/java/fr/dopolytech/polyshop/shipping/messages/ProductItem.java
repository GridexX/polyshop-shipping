package fr.dopolytech.polyshop.shipping.messages;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProductItem {
  public String productId;
  public long amount;

  public ProductItem() {
  }

  public ProductItem(String productId, long amount) {
    this.productId = productId;
    this.amount = amount;
  }
}