package com.example.demo.food;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestaurantController {

    @RequestMapping("/restaurants")
    public Restaurant restaurants() {
        Restaurant le_petit_bleu = new Restaurant("Le petit bleu", "Marocaine, Méditerranéene, Tunisienne");
        return le_petit_bleu;
    }

    @RequestMapping("/again")
    public String anotherApi() {
        return "une autre api";
    }

}
