package com.flexpoker.web.translator;

import com.flexpoker.dto.CreateGameDto;
import com.flexpoker.web.model.CreateGameViewModel;

public class CreateGameTranslator {

    public CreateGameDto translate(CreateGameViewModel viewModel) {
        CreateGameDto gameDto = new CreateGameDto(viewModel.getName(),
                viewModel.getPlayers(), viewModel.getPlayersPerTable());
        return gameDto;
    }

}
