package com.papafila.service;
public final class OrderTracker {
    public static final long PREPARATION_MILLIS=10*60*1000L;
    public long remainingMillis(Order order,long now) { return Math.max(0,PREPARATION_MILLIS-(now-order.placedAtMillis)); }
    public void refresh(Order order,long now) {
        if(order.status()==Order.Status.RETIRADO) return;
        long elapsed=now-order.placedAtMillis;
        order.setStatus(elapsed>=PREPARATION_MILLIS?Order.Status.PRONTO:elapsed>=3000?Order.Status.PREPARANDO:Order.Status.CONFIRMADO);
    }
}
