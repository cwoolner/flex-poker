package com.flexpoker.core.api.handaction;

import java.util.UUID;

import com.flexpoker.model.User;

public interface FoldHandActionCommand {

    void execute(UUID gameId, UUID tableId, User user);

}
