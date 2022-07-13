package com.github.he305.twitchproducer.common.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(Long id);

    List<T> getAll();

    T save(T t);

    void delete(T t);
}
