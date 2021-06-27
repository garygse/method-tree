package com.javanhe.methodtree.test.component;

import com.javanhe.methodtree.test.A;

/**
 * A BaseComponent implementation that returns a greeting.
 */
public class GreetingComponent extends BaseComponent {

    private A a;

    public GreetingComponent() {
        this.a = new A();
    }

    @Override
    public String getMessage() {
        // use case:
        // directly call A.a2()
        // indirectly call B.b2()
        // indirectly call B.b1()
        a.a2();
        return "Hello World!";
    }
}
