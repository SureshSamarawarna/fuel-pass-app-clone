package db;

import java.util.ArrayList;

public class InMemoryDB {
    private static final ArrayList<User> userDatabase = new ArrayList<>();

    static {
        registerUser(new User("123456789V", "Kasun", "Nuwan", "Galle Road, Panadura", 16));
        registerUser(new User("456789123V", "Ruwan", "Sampath", "Galle Road, Panadura", 16));
        registerUser(new User("789456123V", "Supun", "Sampath", "Galle Road, Panadura", 16));
        registerUser(new User("123789456V", "Ruwan", "Jayamal", "Galle Road, Panadura", 16));
    }

    public static boolean registerUser(User newUser) {
        if (findUser(newUser.getNic()) != null) return false;
        userDatabase.add(newUser);
        return true;
    }

    public static User findUser(String nic) {
        for (User user : userDatabase) {
            if (user.getNic().equalsIgnoreCase(nic)) {
                return user;
            }
        }
        return null;
    }

    public static ArrayList<User> findUsers(String query) {
        ArrayList<User> searchResult = new ArrayList<>();
        for (User user : userDatabase) {
            if (user.getNic().contains(query) || user.getFirstName().contains(query) ||
                    user.getLastName().contains(query) || user.getAddress().contains(query)) {
                searchResult.add(user);
            }
        }
        return searchResult;
    }

    public static void removeUser(String nic) {
        User user = findUser(nic);
        if (user != null) userDatabase.remove(user);
    }

    public static ArrayList<User> getUserDatabase() {
        return userDatabase;
    }
}
