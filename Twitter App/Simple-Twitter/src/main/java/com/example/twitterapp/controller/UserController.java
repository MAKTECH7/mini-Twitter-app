    package com.example.twitterapp.controller;

    import org.springframework.ui.Model;
    import com.example.twitterapp.model.User;
    import com.example.twitterapp.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.ModelAttribute;
    import org.springframework.web.bind.annotation.PostMapping;

    @Controller
    public class UserController {
        @Autowired
        private UserDetailsService userDetailsService;
        private UserService userService;

        public UserController(UserService userService){
            this.userService = userService;
        }

        @GetMapping("/login")
        public  String login(Model model, User userDto){
            model.addAttribute("user", userDto);
            return "login";
        }
        // this method return the login page to user

        @GetMapping("register")
        public String register(Model model, User userDto){
            model.addAttribute("user", userDto);
            return "register";
        }
    // this is for registeration

        @PostMapping("/register")
        public String registerSave(@ModelAttribute("user") User userDto, Model model){
            User user = userService.findByUsername(userDto.getUsername());
            if(user!=null){
                model.addAttribute("Userexist", user);
                return "register";
            }
            userService.save(userDto);
            return "redirect:/register?success";
        }
        // this is used for saving and registering the user in h2 database


    }
