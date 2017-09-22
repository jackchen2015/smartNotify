/*
 * Copyright 2015 Hongxin Telecommunication Technologies Co, Ltd.,
 * Wuhan, Hubei, China. All rights reserved.
 */

package com.hxct.notify;

import java.util.LinkedList;

/**
 *
 * @author chenwei
 * Created on 2017-8-16, 15:56:36
 */
public class QueueLink<E> {
    private LinkedList<E> list;

    public QueueLink() {
        list = new LinkedList<E>();
    }
   //入队
    public  void put(E e) {
        list.addLast(e);
    }
   //出队
    public  E pop() {
        return list.removeFirst();
    }

    public  boolean isEmpty() {
        return list.isEmpty();
    }

    public  int size() {
        return list.size();
    }
   //获得队列的第一个元素
    public  E front() {
        return list.getFirst();
    }
}