package model;

import java.util.Collection;
import java.util.Objects;

public class User {
    private static int ID = 0;

    private final int id;
    private String name;
    private double balance;

    //region Ctor
    public User(String name) {
        this(name, 0);
    }

    public User(String name, double balance) {
        this.id = ++ID;
        this.name = name;
        this.balance = balance;
    }
    //endregion

    //region Getters & Setters
    public int getId() { return id; }

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

        if (getId() != user.getId()) return false;
        return getName() != null ? getName().equals(user.getName()) : user.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }


    //endregion

    //region Public Methods

    //region Static Methods
    public static boolean hasUserById(Collection<? extends User> users, int id) {
        return getUserById(users, id) != null;
    }

    public static User getUserById(Collection<? extends User> users, int id) {
        for (User user : users) {
            if (user.getId() == id)
                return user;
        }

        return null;
    }
    //endregion

    //endregion
}
