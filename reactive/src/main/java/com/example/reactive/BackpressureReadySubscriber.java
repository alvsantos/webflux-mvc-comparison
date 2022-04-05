package com.example.reactive;

import jdk.jshell.JShell;
import reactor.core.publisher.BaseSubscriber;

public class BackpressureReadySubscriber<T> extends BaseSubscriber<T> {
 
    public void hookOnSubscribe(JShell.Subscription subscription) {
        //requested the first item on subscribe
        request(1);
    }
 
    public void hookOnNext(T value) {
        //process value
        //processing...
        //once processed, request a next one
        //you can implement specific logic to slow down processing here
        request(1);
    }
}