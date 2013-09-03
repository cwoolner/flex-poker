package com.flexpoker.core.api.scheduling;

public interface ScheduleMoveGameToInProgressCommand {

    void execute(Integer gameId);

}
