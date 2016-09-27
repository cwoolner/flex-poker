package com.flexpoker.signup.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.flexpoker.signup.SignUpUser;

@Profile("default")
@Repository
public class InMemorySignUpRepository implements SignUpRepository {

    private final Set<String> usernameSet;

    private final Map<UUID, Map<String, UUID>> signUpCodeMap;

    private final Map<UUID, SignUpUser> signUpUserMap;

    public InMemorySignUpRepository() {
        usernameSet = new HashSet<>();
        signUpCodeMap = new HashMap<>();
        signUpUserMap = new HashMap<>();
        addDefaultSignUps();
    }

    @Override
    public boolean usernameExists(String username) {
        return usernameSet.contains(username);
    }

    @Override
    public boolean signUpCodeExists(UUID signUpCode) {
        return signUpCodeMap.containsKey(signUpCode);
    }

    @Override
    public UUID findAggregateIdByUsernameAndSignUpCode(String username, UUID signUpCode) {
        Map<String, UUID> foundSignUpCode = signUpCodeMap.get(signUpCode);
        if (foundSignUpCode != null) {
            UUID aggregateId = foundSignUpCode.get(username);
            return aggregateId;
        }

        return null;
    }

    @Override
    public void storeSignUpInformation(UUID aggregateId, String username, UUID signupCode) {
        signUpCodeMap.put(signupCode, new HashMap<>());
        signUpCodeMap.get(signupCode).put(username, aggregateId);
    }

    @Override
    public void storeNewlyConfirmedUsername(String username) {
        usernameSet.add(username);
    }

    @Override
    public UUID findSignUpCodeByUsername(String username) {
        return signUpCodeMap.entrySet().stream()
                .filter(x -> x.getValue().containsKey(username))
                .findAny().get().getKey();
    }

    private void addDefaultSignUps() {
        storeNewlyConfirmedUsername("player1");
        storeNewlyConfirmedUsername("player2");
        storeNewlyConfirmedUsername("player3");
        storeNewlyConfirmedUsername("player4");
    }

    @Override
    public SignUpUser fetchSignUpUser(UUID signUpUserId) {
        return signUpUserMap.get(signUpUserId);
    }

    @Override
    public void saveSignUpUser(SignUpUser signUpUser) {
        signUpUserMap.put(signUpUser.getAggregateId(), signUpUser);
    }

}
