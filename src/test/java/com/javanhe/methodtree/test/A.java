package com.javanhe.methodtree.test;

/**
 * Sample class used for testing purposes.
 */
public class A {

    private B b;

    public A() {
        b = new B();
    }

    public void a1() {
        b.b1();
    }

    public void a2() {
        b.b2();
    }

    public void a3() {
        b.b3();
    }
}
