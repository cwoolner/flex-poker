package com.flexpoker.core.api.game;

import java.security.Principal;
import java.util.UUID;

public interface JoinGameCommand {

    void execute(UUID gameId, Principal principal);

}
