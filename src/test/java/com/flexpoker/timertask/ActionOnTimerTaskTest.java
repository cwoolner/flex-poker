package com.flexpoker.timertask;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flexpoker.core.api.handaction.CallHandActionCommand;
import com.flexpoker.core.api.handaction.FoldHandActionCommand;
import com.flexpoker.core.timertask.ActionOnTimerTask;
import com.flexpoker.model.Game;
import com.flexpoker.model.Seat;
import com.flexpoker.model.Table;
import com.flexpoker.model.User;
import com.flexpoker.model.UserGameStatus;
import com.flexpoker.test.util.datageneration.GameGenerator;
import com.flexpoker.test.util.datageneration.UserGenerator;

public class ActionOnTimerTaskTest {

    private ActionOnTimerTask actionOnTimerTask;
    
    @Mock private CallHandActionCommand callHandActionCommand;
    
    @Mock private FoldHandActionCommand foldHandActionCommand;
    
    private Game game;
    
    private Table table;
    
    private Seat seat;
    
    private User user;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        game = GameGenerator.createGame(2, 2);
        table = new Table();
        user = UserGenerator.createUser("test");
        seat = new Seat(0);
        seat.setUserGameStatus(new UserGameStatus(user, 1500));
        actionOnTimerTask = new ActionOnTimerTask(callHandActionCommand,
                foldHandActionCommand, game, table, seat);
    }
    
    @Test
    public void testFolding() {
        seat.setCallAmount(10);
        actionOnTimerTask.run();
        verify(callHandActionCommand, never()).execute(game.getId(), table.getId(), user);
        verify(foldHandActionCommand, times(1)).execute(game.getId(), table.getId(), user);
    }
    
    @Test
    public void testChecking() {
        seat.setCallAmount(0);
        actionOnTimerTask.run();
        verify(callHandActionCommand, times(1)).execute(game.getId(), table.getId(), user);
        verify(foldHandActionCommand, never()).execute(game.getId(), table.getId(), user);
    }
}
