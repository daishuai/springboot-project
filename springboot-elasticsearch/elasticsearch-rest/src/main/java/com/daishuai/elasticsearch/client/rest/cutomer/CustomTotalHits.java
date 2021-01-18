package com.daishuai.elasticsearch.client.rest.cutomer;

import java.util.Objects;

/**
 * @author Daishuai
 * @date 2021/1/13 19:16
 */
public class CustomTotalHits {

    /** How the {@link CustomTotalHits#value} should be interpreted. */
    public enum Relation {
        /**
         * The total hit count is equal to {@link CustomTotalHits#value}.
         */
        EQUAL_TO,
        /**
         * The total hit count is greater than or equal to {@link CustomTotalHits#value}.
         */
        GREATER_THAN_OR_EQUAL_TO
    }

    /**
     * The value of the total hit count. Must be interpreted in the context of
     * {@link #relation}.
     */
    public final long value;

    /**
     * Whether {@link #value} is the exact hit count, in which case
     * {@link #relation} is equal to {@link Relation#EQUAL_TO}, or a lower bound
     * of the total hit count, in which case {@link #relation} is equal to
     * {@link Relation#GREATER_THAN_OR_EQUAL_TO}.
     */
    public final Relation relation;

    /** Sole constructor. */
    public CustomTotalHits(long value, Relation relation) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be >= 0, got " + value);
        }
        this.value = value;
        this.relation = Objects.requireNonNull(relation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomTotalHits totalHits = (CustomTotalHits) o;
        return value == totalHits.value && relation == totalHits.relation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, relation);
    }

    @Override
    public String toString() {
        return value + (relation == Relation.EQUAL_TO ? "" : "+") + " hits";
    }
}
