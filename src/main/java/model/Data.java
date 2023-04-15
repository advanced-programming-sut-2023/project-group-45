package model;

import java.util.ArrayList;

public abstract class Data {

    private static final ArrayList<User> users = new ArrayList<>();

    ArrayList<User> getUsers() {
        return users;
    }
}
