package com.flexpoker.web.controller

import com.flexpoker.framework.command.CommandSender
import com.flexpoker.login.repository.LoginRepository
import com.flexpoker.table.command.commands.CallCommand
import com.flexpoker.table.command.commands.CheckCommand
import com.flexpoker.table.command.commands.FoldCommand
import com.flexpoker.table.command.commands.RaiseCommand
import com.flexpoker.table.command.commands.TableCommand
import com.flexpoker.table.query.dto.PocketCardsDTO
import com.flexpoker.table.query.dto.TableDTO
import com.flexpoker.table.query.repository.CardsUsedInHandRepository
import com.flexpoker.table.query.repository.TableRepository
import com.flexpoker.web.dto.CallTableActionDTO
import com.flexpoker.web.dto.CheckTableActionDTO
import com.flexpoker.web.dto.FoldTableActionDTO
import com.flexpoker.web.dto.RaiseTableActionDTO
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import java.security.Principal
import java.util.UUID
import javax.inject.Inject

@Controller
class TableController @Inject constructor(
    private val commandSender: CommandSender<TableCommand>,
    private val loginRepository: LoginRepository, private val tableRepository: TableRepository,
    private val cardsUsedInHandRepository: CardsUsedInHandRepository) {

    @SubscribeMapping("/user/queue/pocketcards")
    fun fetchPocketCards(principal: Principal): List<PocketCardsDTO> {
        val playerId = loginRepository.fetchAggregateIdByUsername(principal.name)
        val pocketCardsForUser = cardsUsedInHandRepository.fetchAllPocketCardsForUser(playerId)
        return pocketCardsForUser.map { PocketCardsDTO(it.key, it.value.card1.id, it.value.card2.id) }
    }

    @SubscribeMapping("/topic/game/{gameId}/table/{tableId}")
    fun fetchTable(@DestinationVariable gameId: UUID, @DestinationVariable tableId: UUID): TableDTO {
        return tableRepository.fetchById(tableId)
    }

    @MessageMapping("/app/check")
    fun check(model: CheckTableActionDTO, principal: Principal) {
        val playerId = loginRepository.fetchAggregateIdByUsername(principal.name)
        commandSender.send(CheckCommand(model.tableId, model.gameId, playerId))
    }

    @MessageMapping("/app/fold")
    fun fold(model: FoldTableActionDTO, principal: Principal) {
        val playerId = loginRepository.fetchAggregateIdByUsername(principal.name)
        commandSender.send(FoldCommand(model.tableId, model.gameId, playerId))
    }

    @MessageMapping("/app/call")
    fun call(model: CallTableActionDTO, principal: Principal) {
        val playerId = loginRepository.fetchAggregateIdByUsername(principal.name)
        commandSender.send(CallCommand(model.tableId, model.tableId, playerId))
    }

    @MessageMapping("/app/raise")
    fun raise(model: RaiseTableActionDTO, principal: Principal) {
        val playerId = loginRepository.fetchAggregateIdByUsername(principal.name)
        commandSender.send(RaiseCommand(model.tableId, model.gameId, playerId, model.raiseToAmount))
    }

}