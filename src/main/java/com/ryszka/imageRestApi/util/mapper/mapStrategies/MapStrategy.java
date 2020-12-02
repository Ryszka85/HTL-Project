package com.ryszka.imageRestApi.util.mapper.mapStrategies;

public interface MapStrategy<T, U> {
    U map(T source);
}
