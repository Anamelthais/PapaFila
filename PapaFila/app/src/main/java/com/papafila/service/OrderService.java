package com.papafila.service;
import java.util.Locale;
import java.util.Random;
public final class OrderService {
    private final Random random = new Random();
    public Order confirm(int totalCents) {
        if (totalCents <= 0) throw new IllegalArgumentException("Carrinho vazio");
        String code=String.format(Locale.US,"#%c%03d", (char)('A'+random.nextInt(26)), random.nextInt(1000));
        return new Order(code,totalCents,System.currentTimeMillis());
    }
}
