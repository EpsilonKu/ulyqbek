package kz.bitter.ulyqbek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import kz.bitter.ulyqbek.model.Users;
import kz.bitter.ulyqbek.service.UserService;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    @PreAuthorize("isAnonymous()")
    public String index(Model model) {

        // model.addAttribute("gender", "");
        // model.addAttribute("currentUser", getUserData());
        return "guest/index";
    }

    private Users getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User secUser = (User) authentication.getPrincipal();
            return userService.getUserByEmailorUsername(secUser.getUsername(), secUser.getUsername());
        }
        return null;
    }
}
