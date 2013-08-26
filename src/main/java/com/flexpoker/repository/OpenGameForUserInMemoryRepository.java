package com.flexpoker.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Repository;

import com.flexpoker.model.GameStage;
import com.flexpoker.model.OpenGameForUser;
import com.flexpoker.repository.api.OpenGameForUserRepository;

@Repository
public class OpenGameForUserInMemoryRepository implements OpenGameForUserRepository {

    private final Map<String, List<OpenGameForUser>> openGameForUserMap;

    public OpenGameForUserInMemoryRepository() {
        openGameForUserMap = new HashMap<>();
    }

    @Override
    public List<OpenGameForUser> fetchAllOpenGamesForUser(String username) {
        synchronized (openGameForUserMap) {
            if (!openGameForUserMap.containsKey(username)) {
                openGameForUserMap.put(username, new ArrayList<OpenGameForUser>());                
            }
            return openGameForUserMap.get(username);
        }
    }

    @Override
    public void deleteOpenGameForUser(final String username, final Integer gameId) {
        synchronized (openGameForUserMap) {
            List<OpenGameForUser> openGameForUserList = openGameForUserMap.get(username);
            
            OpenGameForUser openGameForUserToDelete = (OpenGameForUser) CollectionUtils
                    .find(openGameForUserList, new Predicate() {
                
                @Override
                public boolean evaluate(Object openGameForUser) {
                    return gameId == ((OpenGameForUser) openGameForUser).getGameId();
                }
            });
            openGameForUserList.remove(openGameForUserToDelete);
        }
    }

    @Override
    public void addOpenGameForUser(String username, OpenGameForUser openGameForUser) {
        synchronized (openGameForUserMap) {
            if (!openGameForUserMap.containsKey(username)) {
                openGameForUserMap.put(username, new ArrayList<OpenGameForUser>());                
            }
            List<OpenGameForUser> openGameForUserList = openGameForUserMap.get(username);
            openGameForUserList.add(openGameForUser);
        }
    }

    @Override
    public void setGameStage(String username, final Integer gameId, GameStage gameStage) {
        synchronized (openGameForUserMap) {
            List<OpenGameForUser> openGameForUserList = openGameForUserMap.get(username);
            
            OpenGameForUser openGameForUser = (OpenGameForUser) CollectionUtils
                    .find(openGameForUserList, new Predicate() {
                
                @Override
                public boolean evaluate(Object openGameForUser) {
                    return gameId == ((OpenGameForUser) openGameForUser).getGameId();
                }
            });
            openGameForUser.changeGameStage(gameStage);
        }
    }

}
