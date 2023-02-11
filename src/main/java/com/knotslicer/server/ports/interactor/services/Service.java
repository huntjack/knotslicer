package com.knotslicer.server.ports.interactor.services;

public interface Service<T> {
    T create(T t);
    T get(Long id, Long parentId);
    T getWithChildren(Long id, Long parentId);
    T update(T t);
    void delete(Long id, Long parentId);
}
