package com.knotslicer.server.ports.entitygateway;

import java.util.Optional;

public interface ChildDao<T,P> {
    Optional<T> get(Long id);
    T update(T t, Long parentId);
    void delete(Long id, Long parentId);
    Long getPrimaryParentId(Long id);
    Optional<P> getPrimaryParentWithChildren(Long parentId);
}
