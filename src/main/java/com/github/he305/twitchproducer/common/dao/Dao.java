package com.github.he305.twitchproducer.common.dao;

import com.github.he305.twitchproducer.common.exception.EntitySaveFailedException;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface Dao<T, U> {
    Optional<T> get(@NonNull U id);

    List<T> getAll();

    T save(@NonNull T t) throws EntitySaveFailedException;

    void delete(@NonNull T t);
}
