package com.papafila.model;
public final class Product {
    public final String id, name, description, emoji, category;
    public final int priceCents;
    public Product(String id, String name, String description, String emoji, String category, int priceCents) {
        this.id=id; this.name=name; this.description=description; this.emoji=emoji; this.category=category; this.priceCents=priceCents;
    }
}
