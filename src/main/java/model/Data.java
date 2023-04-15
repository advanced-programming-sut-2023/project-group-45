package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Data {

    private static final List<User> users = new ArrayList<>();

    List<User> getUsers() {
        return users;
    }
}
