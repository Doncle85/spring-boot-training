package com.example.demo.hello;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GreetingController {

    @RequestMapping("/greeting")
    public String helloWorld() {
        return "Hello World";
    }

    @RequestMapping("/again")
    public String anotherApi() {
        return "une autre api";
    }

}
