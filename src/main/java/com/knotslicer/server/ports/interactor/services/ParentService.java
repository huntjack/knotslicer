package com.knotslicer.server.ports.interactor.services;

import java.util.Map;

public interface ParentService<T> extends Service<T> {
    T getWithChildren(Map<String,Long> ids);
}
