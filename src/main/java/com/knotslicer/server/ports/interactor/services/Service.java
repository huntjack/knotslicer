package com.knotslicer.server.ports.interactor.services;

import java.util.Map;

public interface Service<T> {
    T create(T t);
    T get(Map<String,Long> ids);
    T update(T t);
    void delete(Map<String,Long> ids);
}
