package com.flexpoker.core.api.chat;

import java.security.Principal;

public interface CreatePersonalChatIdCommand {

    void execute(Principal principal);

}
