package com.javanhe.methodtree.test;

/**
 * Sample class used for testing purposes.
 */
public class C extends B {

    // constructor indirectly calls method b1 (calls to method b2 end up calling method b1)
    public C() {
        b2();
    }
}
