import com.papafila.model.Product;
import com.papafila.service.*;
import java.util.Arrays;
public class DomainTest {
  private static void check(boolean b, String message) { if(!b) throw new AssertionError(message); }
  public static void main(String[] args) {
    Product c = new Product("c", "Coxinha", "", "", "Salgados", 700);
    Cart cart = new Cart(); cart.add(c); cart.add(c);
    check(cart.count()==2 && cart.total(Arrays.asList(c))==1400,"Carrinho e total");
    cart.remove(c); check(cart.count()==1,"Remover produto");
    Order order = new OrderService().confirm(cart.total(Arrays.asList(c)));
    check(order.code.matches("#[A-Z][0-9]{3}"),"Codigo de retirada");
    OrderTracker tracker = new OrderTracker();
    check(tracker.remainingMillis(order,order.placedAtMillis)==600000,"Tempo inicial");
    tracker.refresh(order,order.placedAtMillis+600001);
    check(order.status()==Order.Status.PRONTO,"Pedido pronto");
    order.setStatus(Order.Status.RETIRADO);
    tracker.refresh(order,order.placedAtMillis+700000);
    check(order.status()==Order.Status.RETIRADO,"Retirada preservada");
    System.out.println("Testes de dominio: OK");
  }
}
