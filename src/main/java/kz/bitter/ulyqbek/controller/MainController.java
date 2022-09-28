package kz.bitter.ulyqbek.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kz.bitter.ulyqbek.model.Users;

@Controller
public class MainController {
  @GetMapping( value = "/")
  public String index (Model model){
    return "index";
  }
}
