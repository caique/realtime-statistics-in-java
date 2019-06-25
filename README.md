# Real Time Statistics in Java (Spring Boot)

## Architectural Decisions Records

I truly believe there is no silver bullet in software engineering, therefore, everything comes with a trade-off.

Given that, follows some considerations about the decisions I made while developing this solution. Hope you find this useful when trying to understand why I chose one path instead of another.

---
### 1) Implement repository logic using `ConcurrentHashMap`  
**Context:**

  - The main use case for the API is to calculate realtime statistics for the last 60 seconds of transactions;
  - The solution has to work without a database (this also applies to in-memory databases);
  - The `POST /transactions` and `GET /statistics` endpoints MUST execute in constant time and memory ie O(1);
  - The API has to be threadsafe with concurrent requests.  

**Decistion:**

The class `InMemoryTransactionsRepository` - which is the only concrete implementation of `TransactionsRepository` - could rely in many alternatives data structures that could provide a similar performance and thread-safe behavior (such as `CopyOnWriteArrayList` or `Collections.synchronizedMap`). However, in this case, the choice of a `ConcurrentHashMap` structure makes easier to meet other operational requirements such as non-blocking reading.

**Consequences:**

  - When registering a `Transaction`, two O(1) operations are executed (`get` and `put`);
  - Removal of expired transactions is atomic and takes O(n) given the use `removeIf` on top of the key set;
  - Read operations (including `get`) are non-blocking so the statistics update can occur together with register of new transactions.

---
### 2) Refresh statistics using `Scheduled`  
**Context:**

- The main use case for the API is to calculate realtime statistics for the last 60 seconds of transactions;  
- The `POST /transactions` and `GET /statistics` endpoints MUST execute in constant time and memory ie O(1).  

**Decistion:**

In order to make the result of `GET /statistics` as close as possible of real time, a scheduler runs every 10ms. After removing expired transactions, it calculates the aggregated values and stores the result in an immutable `Statistics` object. Therefore, any request to `GET /statistics` retrieves the most recent `Statistics` object in memory and isn't affected by the quantity of transactions registered.  

**Consequences:**

- Every 10ms the key set in `TransactionsRepository` is traversed and all expired transactions are removed;  
- A request to `GET /statistics` is O(1) since relies on the retrieval of a single object in memory;  
- The `Statistics` object may be outdated once read operations are non-blocking and can coexist with a parallel addition of a new transaction, however, since this is a soft real time system I took the liberty to consider the window of 10ms acceptable.  

---
### 3) Refresh statistics after every operation  
**Context:**

- Statistics are refreshed every 10ms

**Decistion:**

To reduce the risk of having outdated statistics when a request to `GET /statistics` occur, every time a successful request to `POST /transactions` or to `DELETE /transactions` is performed, the `Statistics` object is updated.

**Consequences:**

- Additional executions of `refreshStatistics()` that may be unecessary depending when the last execution happened;
- Reduce risk of outdated values when requesting `GET /statistics`.

---
### 4) Handle timestamp as `Instant`  
**Context:**

- Field `timestamp` is the transaction time in the ISO 8601 format YYYY-MM-DDThh:mm:ss.sssZ in the UTC timezone (this is not the current timestamp)
- `ConcurrentHashMap` needs a immutable and thread-safe key to improve performance

**Decistion:**

By default, `Instant` already consider the timezone and can be easily parsed from an ISO 8601. Also, this field is used as the key to identify a set of transactions stored in a HashMap structure, which makes the choice of `Instant` even more interesting since this class is immutable and thread-safe.

**Consequences:**

- The hashmap key used to group a set of transactions is immutable and thread-safe;
- Implements properly `equals()` and `hashcode()` methods which avoid hash collisions.
