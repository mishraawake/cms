package com.sp.validate.constraint;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Created by pankajmishra on 06/08/16.
 */
public class GreaterThanConstraint implements Constraint<Comparable> {


    Comparable to;

    boolean eq;

    public GreaterThanConstraint(){

    }

    public GreaterThanConstraint(Comparable to, boolean eq){
        this.to = to;
        this.eq = eq;
    }

    @Override
    public boolean pass(Comparable value) {
        return eq ? value.compareTo(to) <= 0 : value.compareTo(to) < 0;
    }

    public Comparable getTo() {
        return to;
    }

    public void setTo(Comparable to) {
        this.to = to;
    }

    public boolean isEq() {
        return eq;
    }

    public void setEq(boolean eq) {
        this.eq = eq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GreaterThanConstraint that = (GreaterThanConstraint) o;

        if (eq != that.eq) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = to != null ? to.hashCode() : 0;
        result = 31 * result + (eq ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GreaterThanConstraint{" +
                "to=" + to +
                ", eq=" + eq +
                '}';
    }
}
