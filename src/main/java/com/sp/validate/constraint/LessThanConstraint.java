package com.sp.validate.constraint;

/**
 * Created by pankajmishra on 06/08/16.
 */
public class LessThanConstraint implements Constraint<Comparable> {

    Comparable from;

    boolean eq;

    public LessThanConstraint(){

    }


    public LessThanConstraint(Comparable from, boolean eq){
        this.from = from;
        this.eq = eq;
    }


    @Override
    public boolean pass(Comparable value) {
        return eq ? from.compareTo(value) <= 0 :  from.compareTo(value) < 0;
    }

    public Comparable getFrom() {
        return from;
    }

    public void setFrom(Comparable from) {
        this.from = from;
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

        LessThanConstraint that = (LessThanConstraint) o;

        if (eq != that.eq) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (eq ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LessThanContraint{" +
                "from=" + from +
                ", eq=" + eq +
                '}';
    }
}
