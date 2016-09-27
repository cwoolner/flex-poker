package com.flexpoker.login.repository;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface LoginRepository extends UserDetailsService {

    void saveUsernameAndPassword(String username, String encryptedPassword);

    UUID fetchAggregateIdByUsername(String username);

    void saveAggregateIdAndUsername(UUID aggregateId, String username);

    String fetchUsernameByAggregateId(UUID createdByPlayerId);

}
