package com.flexpoker.processmanagers;

import com.flexpoker.framework.processmanager.ProcessManager;
import com.flexpoker.signup.command.events.SignedUpUserConfirmedEvent;

public class NewUserProcessManager implements
        ProcessManager<SignedUpUserConfirmedEvent> {

    @Override
    public void handle(SignedUpUserConfirmedEvent event) {
        // TODO Auto-generated method stub

    }

}
