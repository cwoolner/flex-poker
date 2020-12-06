package com.flexpoker.game.command.events

import com.fasterxml.jackson.annotation.JsonProperty
import com.flexpoker.exception.FlexPokerException
import com.flexpoker.game.command.framework.GameEvent
import com.flexpoker.util.StringUtils
import java.time.Instant
import java.util.UUID

/**
 * Base class used to handle some standard methods and fields so that the
 * subclasses can just be specific to what they need.
 */
abstract class BaseGameEvent(private val aggregateId: UUID) : GameEvent {
    private var version = 0
    private val time: Instant
    @JsonProperty("gameId")
    override fun getAggregateId(): UUID {
        return aggregateId
    }

    @JsonProperty
    override fun getVersion(): Int {
        if (version == 0) {
            throw FlexPokerException("should be calling getVersion() in situations where it's already been set")
        }
        return version
    }

    override fun setVersion(version: Int) {
        this.version = version
    }

    @JsonProperty
    override fun getTime(): Instant {
        return time
    }

    override fun toString(): String {
        return StringUtils.allFieldsToString(this)
    }

    init {
        time = Instant.now()
    }
}