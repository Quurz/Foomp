---
title: Trampoline<T> Guide
description: Eliminating stack overflow errors with stackless tail recursion and mutual recursion in Foomp.
---

import { Aside } from '@astrojs/starlight/components';

Java does not support automatic tail-call optimization (TCO). When an algorithm performs deep recursion (such as traversing deep trees, processing large ASTs, or computing large combinatorial functions), it quickly exceeds the JVM stack limit and throws a `StackOverflowError`.

`Trampoline<T>` solves this by turning recursive calls into lightweight data structures (`More` and `Done`). Instead of placing calls onto the JVM call stack, recursion bounces off a trampoline in a flat iterative loop on the heap.

---

## Core Mechanism

A trampolined function returns either:
* `Trampoline.done(result)`: The base case that terminates recursion.
* `Trampoline.more(() -> nextCall(...))`: A suspension thunk representing the next recursive bounce.

When `.get()` is called on a `Trampoline`, it loops until it encounters `Done`:

```
more(() -> ...) ──► more(() -> ...) ──► ... ──► done(value) ──► return value
```

---

## Practical Examples

### 1. Stack-Safe Deep Factorial

```java title="FactorialExample.java"
import org.quurz.foomp.base.util.Trampoline;

import java.math.BigInteger;

import static org.quurz.foomp.base.util.Trampoline.done;
import static org.quurz.foomp.base.util.Trampoline.more;

public class FactorialExample {

    public static BigInteger factorial(long n) {
        return factorialTail(BigInteger.valueOf(n), BigInteger.ONE).get();
    }

    private static Trampoline<BigInteger> factorialTail(BigInteger n, BigInteger acc) {
        if (n.compareTo(BigInteger.ONE) <= 0) {
            return done(acc);
        }
        // Suspension thunk: does not grow stack frames!
        return more(() -> factorialTail(n.subtract(BigInteger.ONE), acc.multiply(n)));
    }

    public static void main(String[] args) {
        // 100,000 recursive steps would normally cause StackOverflowError
        BigInteger result = factorial(100_000);
        System.out.println("Result computed safely without stack overflow!");
    }
}
```

---

### 2. Mutual Recursion (Even / Odd)

Trampolines also seamlessly handle mutual recursion where multiple functions invoke each other recursively:

```java title="MutualRecursionExample.java"
import org.quurz.foomp.base.util.Trampoline;

import static org.quurz.foomp.base.util.Trampoline.done;
import static org.quurz.foomp.base.util.Trampoline.more;

public class MutualRecursionExample {

    public static boolean isEven(int n) {
        return evenTrampoline(n).get();
    }

    private static Trampoline<Boolean> evenTrampoline(int n) {
        if (n == 0) return done(true);
        return more(() -> oddTrampoline(n - 1));
    }

    private static Trampoline<Boolean> oddTrampoline(int n) {
        if (n == 0) return done(false);
        return more(() -> evenTrampoline(n - 1));
    }

    public static void main(String[] args) {
        System.out.println("isEven(1_000_000): " + isEven(1_000_000)); // true
    }
}
```

---

## Best Practices & Performance

:::tip[Heap vs. Stack Trade-Off]
`Trampoline` trades stack memory for heap memory. Each intermediate recursion step allocates a `Trampoline.More` instance and a lambda closure.
:::

:::note[When to Use Trampoline]
* **Recursive business models & ASTs**: Cleanly express complex structural algorithms recursively without worrying about stack depth.
* **Mutual recursion**: Decompose complex state machines or parsers across mutually recursive functions.
* **Hot mathematical loops**: For tight, simple loops where maximum raw throughput is critical, prefer explicit iterative `for` / `while` loops to avoid allocation overhead.
:::
