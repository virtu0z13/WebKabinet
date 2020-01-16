package com.web_kabinet.controller;


import com.web_kabinet.domain.Message;
import com.web_kabinet.domain.User;
import com.web_kabinet.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;
    @GetMapping ("/")
    public String greeting (
            Map<String, Object> model
    ){

        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam (required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages=messageRepo.findAll();

//        if (filter.isEmpty() && filter!=null) {
        messages = messageRepo.findByTagContaining(filter);
//        } else {
//            messages = messageRepo.findAll();
//        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }





    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String text,
                      @RequestParam (required = false, defaultValue = "") String filter,
                      @RequestParam String tag, Map<String, Object> model) {
        if (!text.equals("") && !tag.equals("")) {

            Message message = new Message(text, tag, user);
            messageRepo.save(message);
        }

        Iterable<Message> messages = messageRepo.findAll();
        model.put("messages", messages);
        return "main";
    }

}
