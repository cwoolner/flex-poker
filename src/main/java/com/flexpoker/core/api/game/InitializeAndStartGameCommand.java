package com.flexpoker.core.api.game;

import java.util.UUID;

public interface InitializeAndStartGameCommand {

    void execute(UUID gameId);

}
