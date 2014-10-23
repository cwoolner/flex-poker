package com.flexpoker.signup.command.repository;

import java.util.List;
import java.util.UUID;

import com.flexpoker.signup.command.framework.SignUpEvent;

public interface SignUpEventRepository {

    List<SignUpEvent> fetchAll(UUID id);

    void save(SignUpEvent event);

}
