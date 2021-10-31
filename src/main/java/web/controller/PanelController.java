package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.servise.RoleService;
import web.servise.UserService;
import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;

@Controller
public class PanelController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public PanelController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    //Контроллер основной страницы admin_page
    @GetMapping("admin/users")
    public String listUsers(Model model, Principal principal) {
        model.addAttribute("rolesAuthorizedUser", userService.roleToString(principal));
        model.addAttribute("authorizedUser", userService.getUser(userService.getUserByLogin(principal.getName()).getId()));
        model.addAttribute("listUsers", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "admin_page";
    }

    //Контроллер для создания нового юсера
    @PostMapping("/admin/new")
    public String create(@ModelAttribute("user") User user) {
        Set<Role> roleSet = new LinkedHashSet<>();
        user.getRoles().forEach(role -> roleSet.add(roleService.findRole(role)));
        user.setRoles(roleSet);
        userService.addUser(user);
        return "redirect:/admin/users";
    }
    //Контроллер для редактиования юсера
    @PatchMapping("/admin/{id}")
    public String editUser(@ModelAttribute("user") User user) {
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            Set<Role> roleSet = new LinkedHashSet<>();
            user.getRoles().forEach(role -> roleSet.add(roleService.findRole(role)));
            user.setRoles(roleSet);
        } else {
            user.setRoles(userService.getUser(user.getId()).getRoles());
        }
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    //Контроллер для удаления юсера
    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }

    //Контроллер страницы current_user для юсера
    @GetMapping("/user/user")
    public String getUserById(Model model, Principal principal) {
        model.addAttribute("rolesAuthorizedUser", userService.roleToString(principal));
        model.addAttribute("authorizedUser", userService.getUser(userService.getUserByLogin(principal.getName()).getId()));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "current_user";
    }
}