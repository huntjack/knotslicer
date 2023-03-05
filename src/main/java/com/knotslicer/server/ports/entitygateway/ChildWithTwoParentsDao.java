package com.knotslicer.server.ports.entitygateway;

import java.util.Optional;

public interface ChildWithTwoParentsDao<T,P,S> extends ChildDao<T, P> {
    T create(T t, Long primaryParentId, Long secondaryParentId);
    S getSecondaryParent(Long id);
    Optional<S> getSecondaryParentWithChildren(Long secondaryParentId);
}
