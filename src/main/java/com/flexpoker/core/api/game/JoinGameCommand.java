package com.flexpoker.core.api.game;

import java.security.Principal;

public interface JoinGameCommand {

    void execute(Integer gameId, Principal principal);

}
