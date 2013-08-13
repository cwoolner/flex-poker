package com.flexpoker.core.api.chat;

import java.security.Principal;
import java.util.UUID;

public interface GetPersonalChatIdQuery {

    UUID execute(Principal principal);

}
