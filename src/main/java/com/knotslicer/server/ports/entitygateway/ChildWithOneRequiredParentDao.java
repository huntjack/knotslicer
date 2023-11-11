package com.knotslicer.server.ports.entitygateway;

public interface ChildWithOneRequiredParentDao<T,P> extends ChildDao<T,P> {
    T create(T t, Long parentId);
}
