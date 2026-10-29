package com.papafila.service;
public final class Order {
    public enum Status { CONFIRMADO, PREPARANDO, PRONTO, RETIRADO }
    public final String code;
    public final int totalCents;
    public final long placedAtMillis;
    private Status status = Status.CONFIRMADO;
    public Order(String code, int totalCents, long placedAtMillis) { this.code=code; this.totalCents=totalCents; this.placedAtMillis=placedAtMillis; }
    public Status status() { return status; }
    public void setStatus(Status status) { this.status=status; }
}
