package web.servise;

import web.model.User;

import java.security.Principal;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void addUser(User user);
    User getUserByLogin(String email);
    void deleteUserById(Long id);
    void updateUser(User user);
    User getUser(Long id);

    default String roleToString(Principal principal) {
        return null;
    }
}
