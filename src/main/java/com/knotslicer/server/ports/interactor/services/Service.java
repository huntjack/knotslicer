package com.knotslicer.server.ports.interactor.services;

public interface Service<T> {
    T create(T t);
    T get(Long id);
    T update(T t);
    void delete(Long id);
}
