---
title: Bucket<A>
description: API reference for Bucket, an in-memory batching buffer with overflow flushing in Foomp.
---

`Bucket<A>` is a thread-safe, in-memory batching buffer designed to accumulate elements up to a fixed capacity (`maxSize`) before automatically flushing them to a consumer sink.

It is particularly useful in event pipelines, database batch inserts, file I/O, or message queues where elements arrive individually but must be processed in bulk.

---

## Type Signature

```java
package org.quurz.foomp.base.util;

public class Bucket<A>
```

### Type Parameters
* `A`: The element type accepted and buffered by this bucket.

---

## Static Factory Methods

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `static <A> Bucket<A>` | `bucket(int maxSize, @NonNull Consumer<List<A>> sink)` | Creates a new `Bucket` with the specified maximum batch capacity and target sink. Throws `IllegalArgumentException` if `maxSize < 1`, or `NullPointerException` if `sink` is `null`. |
| `static <A> Collector<A, List<A>, Nothing>` | `toBucket(int maxSize, @NonNull Consumer<List<A>> sink)` | Returns a Java Stream `Collector` that batches incoming stream items through an internal `Bucket` and guarantees a final flush. |

---

## Method Summary

| Return Type | Method | Description |
| :--- | :--- | :--- |
| `void` | `add(@NonNull A element)` | Adds an element to the bucket under a write lock. If `maxSize` is reached, immediately flushes the accumulated batch to the sink and clears the buffer. Throws `NullPointerException` if `element` is `null`. |
| `void` | `flush()` | Explicitly flushes any currently buffered elements to the sink and resets the bucket position to `0`. |

---

## Concurrency & Thread Safety

`Bucket<A>` utilizes a fair `ReentrantReadWriteLock` to guarantee thread safety across concurrent `add` and `flush` invocations:
* **Critical Section:** Adding elements and triggering flushes is synchronized under the write lock.
* **Sink Invocation:** The consumer sink is invoked within the write-lock critical section. If the sink performs long-running or blocking I/O, consider offloading processing or delegating to an asynchronous worker queue.

---

## See Also

* [`Nothing`](/reference/base/util/nothing/) – Void outcome indicator used by `toBucket` stream collector.
* [`Consumer`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Consumer.html) – Standard Java sink callback.
* [`Collector`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/Collector.html) – Java Stream reduction contract.
