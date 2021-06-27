package com.javanhe.methodtree.test.function;

import java.math.BigInteger;
import java.util.function.Supplier;

/**
 * This class is used for testing purposes. It tests for functional interfaces,
 * so searching for 'java/util/function/Supplier' and 'Object get()' should find
 * the correct method in this class.
 */
public class FunctionalTest {

    public void doTask() {
        BigInteger result = squareLazy(() -> {
            return BigInteger.TEN;
        });
        assert BigInteger.valueOf(100).equals(result);
    }

    // we call value.get() twice here...make sure this method only appears in our test results once
    private BigInteger squareLazy(Supplier<BigInteger> value) {
        return value.get().multiply(value.get());
    }
}
