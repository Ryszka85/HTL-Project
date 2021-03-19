package com.ryszka.imageRestApi.util.mapper;

import com.ryszka.imageRestApi.util.mapper.mapStrategies.MapStrategy;

import java.util.List;

public class ObjectMapper {

    public static <T,U> U mapByStrategy(T source, MapStrategy<T, U> strategy) {
        return strategy.map(source);
    }
    public static <T,U> List<U> mapToList(List<T> source, MapStrategy<List<T>, List<U>> strategy) {
        return strategy.map(source);
    }
}
