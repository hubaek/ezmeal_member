package com.teamProject.ezmeal.controller;

import com.teamProject.ezmeal.domain.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SimpleRestController {
    @GetMapping("/test")
    public String test() {
        return "afterTestJSP/test";
    }
    @GetMapping("/ajax")
    public String ajax() {
        return "afterTestJSP/ajax";
    }

    @PostMapping("/send")
    @ResponseBody
    public Person test(@RequestBody Person p) {
        System.out.println("p = " + p);
        p.setName("ABC");
        p.setAge(p.getAge() + 10);

        return p;
    }
}