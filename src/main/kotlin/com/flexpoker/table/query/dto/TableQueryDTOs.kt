package com.flexpoker.table.query.dto

import java.util.UUID

data class CardDTO (val id: Int)

data class PocketCardsDTO (val handId: UUID, val cardId1: Int, val cardId2: Int)

data class PotDTO (val seats: Set<String>, val amount: Int, val isOpen: Boolean, val winners: Set<String>)

data class SeatDTO (val position: Int, val name: String, val chipsInBack: Int, val chipsInFront: Int,
                    val isStillInHand: Boolean, val raiseTo: Int, val callAmount: Int, val isButton: Boolean,
                    val isSmallBlind: Boolean, val isBigBlind: Boolean, val isActionOn: Boolean)

data class TableDTO (val id: UUID, val version: Int, val seats: List<SeatDTO>?, val totalPot: Int, val pots: Set<PotDTO>?,
                     val visibleCommonCards: List<CardDTO>?, val currentHandMinRaiseToAmount: Int, val currentHandId: UUID?)

data class OpenTableForUserDTO(val gameId: UUID, val tableId: UUID)
