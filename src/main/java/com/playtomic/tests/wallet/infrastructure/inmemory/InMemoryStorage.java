package com.playtomic.tests.wallet.infrastructure.inmemory;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStorage<K, V> {
  private final ConcurrentHashMap<K, V> storage;

  public InMemoryStorage() {
    this.storage = new ConcurrentHashMap<>();
  }

  public void put(K key, V value) {
    storage.put(key, value);
  }

  public Optional<V> get(K key) {
    return Optional.ofNullable(storage.get(key));
  }
}
