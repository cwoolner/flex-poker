package com.flexpoker.model.card;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class PocketCards {

    private final Card card1;

    private final Card card2;

    public PocketCards(Card card1, Card card2) {
        this.card1 = card1;
        this.card2 = card2;
    }

    public Card getCard1() {
        return card1;
    }

    public Card getCard2() {
        return card2;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(card1).append(card2).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        PocketCards rhs = (PocketCards) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj))
                .append(card1, rhs.card1).append(card2, rhs.card2).isEquals();
    }

}
