package com.example.demo.food;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestaurantController {

    @RequestMapping("/restaurants")
    public List<Restaurant> restaurants() {

        Restaurant lePetitBleu = new Restaurant("Le petit bleu", "Marocaine, Méditerranéene, Tunisienne");
        Restaurant chezPierrot = new Restaurant("Chez Pierrot", "Italien");


        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(lePetitBleu);
        restaurants.add(chezPierrot);

        return restaurants;
    }

    @RequestMapping("/again")
    public String anotherApi() {
        return "une autre api";
    }

}
