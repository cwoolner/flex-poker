package com.flexpoker.core.api.handaction;

import java.util.UUID;

import com.flexpoker.model.User;

public interface RaiseHandActionCommand {

    void execute(UUID gameId, UUID tableId, int raiseAmount, User user);

}
