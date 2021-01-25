package com.example.springbootsec;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MySecretController {

    @GetMapping
    @RequestMapping("secret")
    public String mySecret(){
        return "secret!";
    }

    @GetMapping
    @RequestMapping("secret2")
    public String mySecret2(){
        return "secret2!";
    }

    @GetMapping
    @RequestMapping("not-secret")
    public String notSecret(){
        return "not-secret!";
    }


}
