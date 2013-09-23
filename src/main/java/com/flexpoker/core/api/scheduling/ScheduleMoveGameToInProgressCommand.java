package com.flexpoker.core.api.scheduling;

import java.util.UUID;

public interface ScheduleMoveGameToInProgressCommand {

    void execute(UUID gameId);

}
