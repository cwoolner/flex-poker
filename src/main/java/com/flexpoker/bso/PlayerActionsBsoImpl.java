package com.flexpoker.bso;

import java.security.Principal;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.flexpoker.bso.api.PlayerActionsBso;
import com.flexpoker.core.api.handaction.CallHandActionCommand;
import com.flexpoker.core.api.handaction.CheckHandActionCommand;
import com.flexpoker.core.api.handaction.FoldHandActionCommand;
import com.flexpoker.core.api.handaction.RaiseHandActionCommand;
import com.flexpoker.model.User;
import com.flexpoker.repository.api.UserRepository;

@Service
public class PlayerActionsBsoImpl implements PlayerActionsBso {

    private final UserRepository userRepository;
    
    private final CheckHandActionCommand checkHandActionCommand;
    
    private final CallHandActionCommand callHandActionCommand;
    
    private final FoldHandActionCommand foldHandActionCommand;
    
    private final RaiseHandActionCommand raiseHandActionCommand;
    
    @Inject
    public PlayerActionsBsoImpl(
            UserRepository userRepository,
            CheckHandActionCommand checkHandActionCommand,
            CallHandActionCommand callHandActionCommand,
            FoldHandActionCommand foldHandActionCommand,
            RaiseHandActionCommand raiseHandActionCommand) {
        this.userRepository = userRepository;
        this.checkHandActionCommand = checkHandActionCommand;
        this.callHandActionCommand = callHandActionCommand;
        this.foldHandActionCommand = foldHandActionCommand;
        this.raiseHandActionCommand = raiseHandActionCommand;
    }

    @Override
    public void check(UUID gameId, UUID tableId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        checkHandActionCommand.execute(gameId, tableId, user);
    }

    @Override
    public void fold(UUID gameId, UUID tableId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        foldHandActionCommand.execute(gameId, tableId, user);
    }

    @Override
    public void call(UUID gameId, UUID tableId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        callHandActionCommand.execute(gameId, tableId, user);
    }

    @Override
    public void raise(UUID gameId, UUID tableId, int raiseToAmount, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        raiseHandActionCommand.execute(gameId, tableId, raiseToAmount, user);
    }

}
