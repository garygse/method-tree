# Method Tree

A utility that helps identify which classes and methods in a code base make calls, either directly or indirectly, to a specific method.

### Background

The idea behind this project was born when there was a desire to help our QA testers identify what needs to be tested
when changes are made to the code. This project aims to answer one part of the problem - namely:

* what other code touches the code that we've just modified?

Next steps to take include:

* create a pipeline plugin that is able to identify all relevant commit changes made
* integrate this project to identify all other code in the code base that touches the modified code
* inspect the impacted classes to identify potential testing points

### Usage Scenarios

Suppose a project uses REST endpoints...wouldn't it be nice to inspect all potentially impacted classes and see if any
of them are annotated with, say, Spring REST annotations? That would tell us which endpoints QA will need to concentrate 
on in.

For small projects this is trivial, but some projects are huge, and changing code in one place can have
far-reaching implications throughout the system. Are there any batch processes that need to be tested? How about user
interaction points? In an ideal world, regression test suites would cover all of this for us, but unfortunately
we live in the real world.

Hopefully this project will become part of a bigger solution that remedies this.

### Technical 

This project uses ASM under the covers to inspect the bytecode of the classes in order to find where methods get called from.
It then uses recursion to then find where those methods themselves are being called, and so on.

As it is bytecode-based, that means that any code transformations (Lombok annotations etc) will automatically be taken into account.

### Caveats

* code that is called via reflection or by other frameworks (eg Spring) will not be detected and is beyond the scope of this tool
* as this tool is bytecode-based, do not try looking for generics (hello there type erasure!) :)

For example, given the following code:
```java
    public interface Loader<T> {
        T load();
    }

    public class LazyLoader implements Loader<Properties> {
        public Properties load() {
            // implementation
        }
    }
```

the call to the `LazyLoader.load()` method will return an `Object`, so searching for usage of the method `java.util.Properties load()` will return nothing,
while searching for `Object load()` will yield results.

### License

This code is released under the Apache License, Version 2.0, the details of which may be found in the LICENSE file.
