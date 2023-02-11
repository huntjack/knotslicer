package com.knotslicer.server.ports.entitygateway;

public interface ChildWithOneParentDao<T,P> extends ChildDao<T,P> {
    T create(T t, Long parentId);
}
