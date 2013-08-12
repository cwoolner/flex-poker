package com.flexpoker.model.chat.outgoing;

import java.util.Collections;
import java.util.List;

public class PersonalChatMessage extends BaseOutgoingChatMessage {

    private final List<String> receiverUsernames;

    public PersonalChatMessage(String message, String senderUsername, boolean isSystemMessage, List<String> receiverUsernames) {
        super(message, senderUsername, isSystemMessage);
        this.receiverUsernames = receiverUsernames;
    }

    public List<String> getReceiverUsernames() {
        return Collections.unmodifiableList(receiverUsernames);
    }
}
