/*
 * Copyright 2015 Hongxin Telecommunication Technologies Co, Ltd.,
 * Wuhan, Hubei, China. All rights reserved.
 */

package com.hxct.notify;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chenwei
 * Created on 2017-8-16, 15:56:36
 */
public class QueueLink<E> {
    private List<E> list;

    public QueueLink() {
        list = new ArrayList<E>();
    }
   //入队
    public  void put(E e) {
        list.add(e);
    }
   //出队
    public  E pop() {
        return list.remove(0);
    }

    public  boolean isEmpty() {
        return list.isEmpty();
    }

    public  int size() {
        return list.size();
    }
   //获得队列的第一个元素
    public  E front() {
        return list.get(0);
    }
}