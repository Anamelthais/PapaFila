package com.papafila.data;
import com.papafila.model.Product;
import java.util.Arrays;
import java.util.List;
public final class DemoCatalogRepository implements CatalogRepository {
    @Override public List<Product> list() { return Arrays.asList(
        new Product("cox", "Coxinha", "Frango desfiado e massa crocante", "🍗", "Salgados", 600),
        new Product("kib", "Kibe", "Bem temperado e crocante", "🥟", "Salgados", 600),
        new Product("pas", "Pastel", "Recheio de queijo", "🥟", "Salgados", 700),
        new Product("emp", "Empada", "Massa amanteigada", "🥧", "Salgados", 600),
        new Product("pao", "Pão de queijo", "Quentinho e macio", "🧀", "Salgados", 500),
        new Product("suc", "Suco de laranja", "Copo de 300 ml", "🍊", "Bebidas", 500),
        new Product("ref", "Refrigerante", "Lata de 350 ml", "🥤", "Bebidas", 600),
        new Product("com", "Combo intervalo", "Coxinha + suco", "🍗", "Combos", 1000)
    ); }
}
