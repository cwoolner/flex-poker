package com.flexpoker.table.command.aggregate

/**
 * This enum represents the various states that a dealer goes through.  When a
 * new hand is started, the dealer begins to deal the pocket cards.  After,
 * the flop, turn, and river are dealt.
 *
 * The state is updated in memory as the hand progresses.  The state will be
 * updated before any users are made aware of the state update via messages.
 * Because of this, the enum can have confusing meanings depending on which part
 * of the application it is used.
 *
 * For example:
 *
 * The big blind user checks which causes the flop to be dealt.  The state will
 * be updated in the check() method to be FLOP_DEALT (past tense) even though no
 * players have actually been notified that it is time to deal the flop.  The
 * event manager will then send an event to the users saying that it's time to
 * deal the flop (future tense) even though the state in memory acts as if the
 * flop has already been dealt (past tense).  After another round of betting,
 * the application will read the state as FLOP_DEALT and properly determine that
 * the new correct state is TURN_DEALT.  After this change, the event manager
 * will be able to message the clients that they are now able to retrieve the
 * turn card.
 *
 * NOTE: The ordering of the enum constants is important as the ordinal() value
 * is used for security check purposes using ranges.  For instance, if the
 * hand is in the TURN_DEALT state, then a client will be able to fetch
 * their pocket cards, the flop, and the turn, but not the river.
 */
enum class HandDealerState {
    NONE, POCKET_CARDS_DEALT, FLOP_DEALT, TURN_DEALT, RIVER_DEALT, COMPLETE
}