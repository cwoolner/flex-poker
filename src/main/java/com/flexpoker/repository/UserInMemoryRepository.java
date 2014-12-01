package com.flexpoker.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.User;
import com.flexpoker.repository.api.UserRepository;

@Repository
public class UserInMemoryRepository implements UserRepository {

    private final Map<String, User> inMemoryUserList;

    public UserInMemoryRepository() {
        inMemoryUserList = new HashMap<>();
        User player1User = new User(UUID.randomUUID(), "player1");
        User player2User = new User(UUID.randomUUID(), "player2");
        User player3User = new User(UUID.randomUUID(), "player3");
        User player4User = new User(UUID.randomUUID(), "player4");
        inMemoryUserList.put(player1User.getUsername(), player1User);
        inMemoryUserList.put(player2User.getUsername(), player2User);
        inMemoryUserList.put(player3User.getUsername(), player3User);
        inMemoryUserList.put(player4User.getUsername(), player4User);
    }

    @Override
    public User findByUsername(String username) {
        return inMemoryUserList.get(username);
    }

    @Override
    public void insertUser(User signupUser) {
        inMemoryUserList.put(signupUser.getUsername(), signupUser);
    }

    @Override
    public void updateUser(User user) {
        // do nothing for now. when we move to redis or some other db store,
        // this should be implemented then
    }

}
