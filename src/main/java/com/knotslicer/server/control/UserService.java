package com.knotslicer.server.control;

import com.knotslicer.server.entity.User;

public interface UserService {
    public long createUser(User userDto);
}
