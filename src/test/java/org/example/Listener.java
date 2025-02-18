package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.Method;

public class Listener implements WebDriverListener {

    @Override
    public void beforeAnyCall(Object target, Method method, Object[] args) {
        System.out.println("beforeAnyCall {}, {}, {}" + target.toString() + method.getName().toString() + args.toString());
    }

    @Override
    public void afterAnyCall(Object target, Method method, Object[] args, Object result) {
        System.out.println("afterAnyCall {}, {}, {}" + target.toString() + method.getName().toString() + result.toString());

    }



}

