package com.flexpoker.core.api.handaction;

import java.util.UUID;

import com.flexpoker.model.User;

public interface CheckHandActionCommand {

    void execute(UUID gameId, UUID tableId, User user);

}
