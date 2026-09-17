---
title: Bucket Guide
description: Practical developer guide for in-memory batching and buffered sinks using Bucket in Foomp.
---

`Bucket<A>` provides an in-memory buffer that accumulates items up to a specified capacity (`maxSize`) and flushes them in batches to a consumer sink.

---

## When to Use `Bucket<A>`

* **Batching Writes & Network Requests:** Grouping individual items into batches for database inserts (`INSERT INTO ... VALUES (...)`), HTTP POST requests, or message brokers (Kafka, RabbitMQ, SQS).
* **Event & Callback Pipelines:** Buffering high-frequency events or log messages before dispatching them to disk or downstream consumers.
* **Stream Batching:** Accumulating items from a Java `Stream` into chunks via `toBucket(maxSize, sink)`.

---

## Examples

### 1. Basic Batching with Explicit Ingestion

```java title="BucketBasicExample.java"
import org.quurz.foomp.base.util.Bucket;

import java.util.List;

import static org.quurz.foomp.base.util.Bucket.bucket;

public class BucketBasicExample {
    public static void main(String[] args) {
        // Create a bucket with batch size 3 and a printing sink
        Bucket<String> batcher = bucket(3, batch -> {
            System.out.println("Flushing batch of " + batch.size() + " items: " + batch);
        });

        // Add items one by one
        batcher.add("User 1");
        batcher.add("User 2");
        batcher.add("User 3"); // Triggers automatic flush: [User 1, User 2, User 3]

        batcher.add("User 4");
        batcher.add("User 5");

        // Flush remaining buffered items manually
        batcher.flush(); // Flushes: [User 4, User 5]
    }
}
```

---

### 2. Stream Batching with `toBucket`

You can collect elements directly from Java `Stream`s into batches:

```java title="BucketStreamExample.java"
import org.quurz.foomp.base.util.Bucket;

import java.util.stream.IntStream;

public class BucketStreamExample {
    public static void main(String[] args) {
        IntStream.rangeClosed(1, 10)
            .boxed()
            .collect(Bucket.toBucket(4, batch -> {
                System.out.println("Processed chunk: " + batch);
            }));

        // Output:
        // Processed chunk: [1, 2, 3, 4]
        // Processed chunk: [5, 6, 7, 8]
        // Processed chunk: [9, 10]
    }
}
```

---

### 3. Thread-Safe Concurrent Ingestion

`Bucket<A>` uses a fair `ReentrantReadWriteLock` internally, ensuring multiple worker threads can feed the same bucket safely:

```java title="BucketConcurrentExample.java"
import org.quurz.foomp.base.util.Bucket;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.quurz.foomp.base.util.Bucket.bucket;

public class BucketConcurrentExample {
    public static void main(String[] args) throws InterruptedException {
        Bucket<Integer> sharedBucket = bucket(100, batch -> {
            System.out.println("Flushed " + batch.size() + " items on thread: " + Thread.currentThread().getName());
        });

        try (var executor = Executors.newFixedThreadPool(4)) {
            for (int i = 0; i < 4; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    for (int j = 0; j < 50; j++) {
                        sharedBucket.add(threadId * 1000 + j);
                    }
                });
            }
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }

        // Ensure leftover items are flushed
        sharedBucket.flush();
    }
}
```

---

## Best Practices

:::tip[Batching Considerations]
* **Always Flush at Termination:** Ensure you call `.flush()` when your producer finishes or during application shutdown to avoid leaving unflushed items in the buffer.
* **Keep Sinks Fast:** The configured sink callback is invoked while holding the write lock. If the sink performs heavy or blocking I/O, consider handing off the flushed list to an asynchronous executor queue.
:::
