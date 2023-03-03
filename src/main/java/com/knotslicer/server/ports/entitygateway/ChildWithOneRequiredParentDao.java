package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.User;

public interface ChildWithOneRequiredParentDao<T,P> extends ChildDao<T,P> {
    T create(T t, Long parentId);
}
