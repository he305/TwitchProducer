package com.github.he305.twitchproducer.common.dao;

import com.github.he305.twitchproducer.common.exception.EntitySaveFailedException;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(Long id);

    List<T> getAll();

    T save(T t) throws EntitySaveFailedException;

    void delete(T t);
}
