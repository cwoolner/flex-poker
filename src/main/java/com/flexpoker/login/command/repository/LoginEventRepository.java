package com.flexpoker.login.command.repository;

import java.util.List;
import java.util.UUID;

import com.flexpoker.login.command.framework.LoginEvent;

public interface LoginEventRepository {

    List<LoginEvent> fetchAll(UUID id);

    void save(LoginEvent event);

}
