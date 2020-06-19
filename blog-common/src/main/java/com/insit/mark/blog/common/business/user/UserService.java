package com.insit.mark.blog.common.business.user;

import com.insit.mark.blog.common.persistence.model.User;

import java.util.List;

public interface UserService {

    User findUser(String userName);
}
