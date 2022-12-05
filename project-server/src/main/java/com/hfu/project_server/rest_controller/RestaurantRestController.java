package com.hfu.project_server.rest_controller;

import com.google.common.base.Strings;
import com.hfu.project_server.entity.Restaurant;
import com.hfu.project_server.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class RestaurantRestController {
    private final RestaurantService restaurantService;

    @GetMapping("/restaurants")
    public ResponseEntity<?> getRestaurants(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category", required = false) String category
    ) {

        List<Restaurant> restaurants = restaurantService.getRestaurantByQueryApi(name, category);
        return ResponseEntity.ok().body(restaurants);
    }

    @GetMapping("/restaurants/latest")
    public ResponseEntity<?> getLatest() {
        log.info("GET: get latest restaurants.");
        return ResponseEntity.ok().body(restaurantService.getLatestFive());
    }


    // Post
    // Put
    // Delete
}
