public class Temp {
    /*@ public normal_behavior
      @ requires !(\exists int i; 0 <= i && i < messages.length; messages[i].equals(message)));
      @ assignable messages, products, countOfProducts;
      @ ensures messages.length == \old(messages.length) + 1;
      @ ensures (\forall int i; 0 <= i && i < \old(messages.length);
      @          (\exists int j; 0 <= j && j < messages.length; messages[j].equals(\old(messages[i]))));
      @ ensures (\exists int i; 0 <= i && i < messages.length; messages[i].equals(message));
      @ ensures products.length == \old(products.length) + 1;
      @ ensures (\forall int i; 0 <= i && i < \old(products.length);
      @          (\exists int j; 0 <= j && j < products.length; products[j].equals(\old(products[i]))));
      @ ensures (\exists int i; 0 <= i && i < products.length; products[i].equals(product));
      @ ensures countOfProducts.get(products) == \old(countOfProducts.get(products)) + 1;
      @ also
      @ public exceptional_behavior
      @ signals (EqualMessageIdException e) (\exists int i; 0 <= i && i < messages.length;
      @                                     messages[i].equals(message));
      @*/
    public void addProductMessage(Product product);

    /*@ public normal_behavior
      @ requires containsMessage(id) && countOfProducts.get(product) != 0;
      @ assignable messages, countOfProducts, advertise.advertiserProduct;
      @ ensures !containsMessage(id) && messages.length == \old(messages.length) - 1 &&
      @         (\forall int i; 0 <= i && i < \old(messages.length) && \old(messages[i].getId()) != id;
      @         (\exists int j; 0 <= j && j < messages.length; messages[j].equals(\old(messages[i]))));
      @ ensures advertise.advertiserProduct.contains(products)
      @ ensures (\forall int i; 0 <= i && i < \old(advertise.advertiserProduct.size());
                    (\exits int j; 0 <= j && j < advertise.advertiserProduct.size();
                    old\(advertise.advertiserProduct.get(i)) == advertise.advertiserProduct.get(j)));
      @ also
      @ public exceptional_behavior
      @ signals (MessageIdNotFoundException e) !containsMessage(id);
      @ signals (ProductNotFoundException e) countOfProducts.get(product) == 0;
     */

    /*@ public normal_behavior
      @ requires containsMessage(id) && countOfProducts.get(product) != 0 && advertise.advertiserProduct.contains(preference);
      @ assignable nothing;
      @ ensures \result == preference
      @ also
      @ public normal_behavior
      @ requires containsMessage(id) && countOfProducts.get(product) != 0 && !advertise.advertiserProduct.contains(preference);
      @ assignable nothing;
      @ ensures \result == null
      @ also
      @ public exceptional_behavior
      @ signals (MessageIdNotFoundException e) !containsMessage(id);
      @ signals (ProductNotFoundException e) countOfProducts.get(product) == 0;
     */
}
