package com.playtomic.tests.wallet.infrastructure.inmemory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryStorage<K, V> {
  private final ConcurrentHashMap<K, V> storage;
  private final Function<V, V> copyFunction;

  public InMemoryStorage(Function<V, V> copyFunction) {
    this.storage = new ConcurrentHashMap<>();
    this.copyFunction = copyFunction;
  }

  public void put(K key, V value) {
    storage.put(key, value);
  }

  public Optional<V> get(K key) {
    return Optional.ofNullable(storage.get(key)).map(copyFunction);
  }

  public Optional<V> findOne(Predicate<V> predicate) {
    List<V> found = storage.values().stream().filter(predicate).collect(Collectors.toList());

    if (found.isEmpty()) {
      return Optional.empty();
    } else if (found.size() > 1) {
      throw new RuntimeException();
    }

    return Optional.of(found.get(0));
  }
}
