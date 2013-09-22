package com.flexpoker.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.flexpoker.model.User;
import com.flexpoker.repository.api.UserRepository;

@Repository
public class UserInMemoryRepository implements UserRepository {

    private final Map<String, User> inMemoryUserList;
    
    public UserInMemoryRepository() {
        inMemoryUserList = new HashMap<>();
        User player1User = new User();
        User player2User = new User();
        User player3User = new User();
        User player4User = new User();
        player1User.setId(1);
        player1User.setUsername("player1");
        player2User.setId(2);
        player2User.setUsername("player2");
        player3User.setId(3);
        player3User.setUsername("player3");
        player4User.setId(4);
        player4User.setUsername("player4");
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
        // do nothing for now.  when we move to redis or some other db store,
        // this should be implemented then
    }

}
