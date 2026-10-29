package com.papafila.service;
import com.papafila.model.Product;
import java.util.LinkedHashMap;
import java.util.Map;
public final class Cart {
    private final Map<String,Integer> quantities = new LinkedHashMap<>();
    public void add(Product product) { quantities.put(product.id, quantity(product)+1); }
    public void remove(Product product) { int n=quantity(product); if(n<=1) quantities.remove(product.id); else quantities.put(product.id,n-1); }
    public int quantity(Product product) { return quantities.getOrDefault(product.id,0); }
    public int count() { int n=0; for(int q:quantities.values()) n+=q; return n; }
    public int total(Iterable<Product> products) { int cents=0; for(Product p:products) cents+=p.priceCents*quantity(p); return cents; }
    public void clear() { quantities.clear(); }
}
