package com.flexpoker.web.controller

import com.flexpoker.framework.command.CommandSender
import com.flexpoker.game.command.commands.CreateGameCommand
import com.flexpoker.game.command.commands.GameCommand
import com.flexpoker.game.command.commands.JoinGameCommand
import com.flexpoker.game.query.dto.GameInListDTO
import com.flexpoker.game.query.dto.OpenGameForUser
import com.flexpoker.game.query.repository.GameListRepository
import com.flexpoker.game.query.repository.OpenGameForPlayerRepository
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.web.dto.CreateGameDTO
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import java.security.Principal
import java.util.UUID
import javax.inject.Inject

@Controller
class GameManagementController @Inject constructor(
    private val openGameForUserRepository: OpenGameForPlayerRepository,
    private val commandSender: CommandSender<GameCommand>,
    private val loginRepository: LoginRepository,
    private val gameRepository: GameListRepository) {

    @SubscribeMapping("/topic/availabletournaments")
    fun displayAllGames(): List<GameInListDTO> {
        return gameRepository.fetchAll()
    }

    @SubscribeMapping("/app/opengamesforuser")
    fun displayOpenGames(principal: Principal): List<OpenGameForUser>? {
        val playerId = loginRepository.fetchAggregateIdByUsername(principal.name)
        return openGameForUserRepository.fetchAllOpenGamesForPlayer(playerId)
    }

    @MessageMapping("/app/creategame")
    fun createGame(model: CreateGameDTO, principal: Principal) {
        val playerId = loginRepository.fetchAggregateIdByUsername(principal.name)
        val command = CreateGameCommand(model.name, model.players, model.playersPerTable, playerId,
            model.numberOfMinutesBetweenBlindLevels, model.numberOfSecondsForActionOnTimer)
        commandSender.send(command)
    }

    @MessageMapping("/app/joingame")
    fun joinGame(gameId: UUID, principal: Principal) {
        val playerId = loginRepository.fetchAggregateIdByUsername(principal.name)
        val command = JoinGameCommand(gameId, playerId)
        commandSender.send(command)
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    fun handleException(exception: Throwable): String? {
        return exception.message
    }

}