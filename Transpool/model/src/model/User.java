package model;

import java.util.Objects;

public class User {
    private String name;
    private double balance;

    //region Ctor
    public User(String name) {
        this(name, 0);
    }

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }
    //endregion

    //region Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    //endregion

    //region Equals & HashCode Overrides

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return getName() != null ? getName().equals(user.getName()) : user.getName() == null;
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    //endregion

    // TODO: Complete Mocks

    public void OfferTrip() {

    }

    public Object RequestTrip() {
        return null;
    }
}
