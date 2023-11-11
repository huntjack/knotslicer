package com.knotslicer.server.ports.interactor.services;

public interface ParentService<T> extends Service<T> {
    T getWithChildren(Long id);
}
