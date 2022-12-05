package com.hfu.project_server.service.impl;

import com.google.common.base.Strings;
import com.hfu.project_server.entity.Restaurant;
import com.hfu.project_server.repository.RestaurantRepository;
import com.hfu.project_server.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> getRestaurantByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name).orElse(null);
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id).get();
    }

    @Override
    public List<Restaurant> getRestaurantByCategory(String category) {
        return restaurantRepository.findByCategory(category).orElse(null);
    }

    @Override
    public List<Restaurant> getLatestFive() {
        return restaurantRepository.findTop5ByOrderByIdDesc().get();
    }

    /**
     * （待修正）
     * @param restaurant 欲新增（或修改）的餐廳物件。
     */
    @Override
    public void addRestaurant(Restaurant restaurant) {

        String address = restaurant.getAddress();
        boolean isAddressUsed = restaurantRepository.findByAddressContainingIgnoreCase(address).isPresent();

        if(isAddressUsed) {
            log.error("Add: address duplicated!");
        } else {
            restaurantRepository.save(restaurant);
            log.info("Add: successfully add new restaurant: {}", restaurant);
        }
    }

    /**
     * 更新餐廳資訊
     *
     * @param restaurant
     */
    @Override
    public void updateRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        log.info("Update: successfully update the restaurant: {}", restaurant);
    }

    @Override
    public List<Restaurant> getRestaurantByQuery(String type, String query) {

        if (Strings.isNullOrEmpty(type) || Strings.isNullOrEmpty(query)) {
            return getRestaurants();
        }

        switch (type) {

            case "name" :
                return getRestaurantByName(query);

            case "address" :
                return restaurantRepository.findByAddressContainingIgnoreCase(query).orElse(null);

            case "category" :
                return getRestaurantByCategory(query);

            default:
                return null;
        }
    }

    @Override
    public void deleteRestaurantById(Long id) {
        restaurantRepository.deleteById(id);
        log.info("Delete: successfully delete 1 restaurant, id: {}", id);
    }

    /**
     * 獲取餐廳（API用）
     * TODO:這方法太多餘，應該合併
     */

    @Override
    public List<Restaurant> getRestaurantByQueryApi(String nameQuery, String categoryQuery) {

        if (!Strings.isNullOrEmpty(nameQuery)){
            return getRestaurantByName(nameQuery);
        }

        if (!Strings.isNullOrEmpty(categoryQuery)) {
            return getRestaurantByCategory(categoryQuery);
        }

        return getRestaurants();
    }
}
