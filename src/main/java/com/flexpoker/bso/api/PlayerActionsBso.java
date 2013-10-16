package com.flexpoker.bso.api;

import java.security.Principal;
import java.util.UUID;

public interface PlayerActionsBso {

    void check(UUID gameId, UUID tableId, Principal principal);

    void fold(UUID gameId, UUID tableId, Principal principal);

    void call(UUID gameId, UUID tableId, Principal principal);

    void raise(UUID gameId, UUID tableId, int raiseToAmount, Principal principal);

}
