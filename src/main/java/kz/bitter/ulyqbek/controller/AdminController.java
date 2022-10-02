package kz.bitter.ulyqbek.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import kz.bitter.ulyqbek.model.Chapters;
import kz.bitter.ulyqbek.model.Lessons;
import kz.bitter.ulyqbek.model.Users;
import kz.bitter.ulyqbek.service.CourseService;
import kz.bitter.ulyqbek.service.UserService;

@Controller
// @RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    // TODO: Migrate user CRUD from Teacher
    private Users getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User secUser = (User) authentication.getPrincipal();
            return userService.getUserByEmailorUsername(secUser.getUsername(), secUser.getUsername());
        }

        return null;
    }
}
