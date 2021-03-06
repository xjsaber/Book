package com.xjsaber.java.thread.code.ch2.readwrite;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xjsaber
 */
public class PricesInfo {

    private double price1;
    private double price2;

    /**
     * 读写锁ReadWriteLock
     */
    private ReadWriteLock lock;

    public PricesInfo(){
        price1 = 1.0;
        price2 = 2.0;
        lock = new ReentrantReadWriteLock();
    }

    public double getPrice1() {
        lock.readLock().lock();
        // 使用读锁获取这个属性的访问权
        double value = price1;
        lock.readLock().unlock();
        return value;
    }

    public double getPrice2(){
        lock.readLock().lock();
        // 使用读锁获取这个属性的访问权
        double value = price2;
        lock.readLock().unlock();
        return value;
    }

    public void setPrices(double price1, double price2){
        lock.writeLock().lock();
        this.price1 = price1;
        this.price2 = price2;
        lock.writeLock().unlock();
    }
}
