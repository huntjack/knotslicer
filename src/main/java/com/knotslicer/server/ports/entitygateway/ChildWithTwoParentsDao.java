package com.knotslicer.server.ports.entitygateway;

public interface ChildWithTwoParentsDao<T,P> extends ChildDao<T, P> {
    T create(T t, Long primaryParentId, Long secondaryParentId);
    Long getSecondaryParentId(Long id);
}
