package com.hfu.project_server.service;

import com.hfu.project_server.entity.Restaurant;

import java.util.List;

public interface RestaurantService {

    /**
     * 取得全部餐廳資訊
     *
     * @return List集合之所有餐廳資訊。
     */
    List<Restaurant> getRestaurants();

    /**
     * 依名稱查詢餐廳
     *
     * @param name：餐廳名稱。
     * @return List集合之符合條件餐廳。
     */
    List<Restaurant> getRestaurantByName(String name);

    /**
     * 依id查詢餐廳
     *
     * @param id 餐廳id
     * @return List集合之符合條件餐廳。
     */
    Restaurant getRestaurantById(Long id);

    /**
     * 依類型查詢餐廳
     *
     * @param category 餐廳類型。
     * @return List集合之符合條件餐廳。
     */
    List<Restaurant> getRestaurantByCategory(String category);

    /**
     * 取得五個最新餐廳
     *
     * @return 回傳五個最新的餐廳。
     */
    List<Restaurant> getLatestFive();

    /**
     * 儲存餐廳資訊
     *
     * 新增或刪除都調用此方法。
     *
     * @param restaurant 欲新增（或修改）的餐廳物件。
     */
    void addRestaurant(Restaurant restaurant);

    /**
     * 更新餐廳資訊
     * @param restaurant
     */
    void updateRestaurant(Restaurant restaurant);

    /**
     * 透過QueryString搜尋Restaurant
     *
     * 當條件輸入不完全（有一方為空），則預設回傳全部的餐廳列表。
     *
     * @param type: 選擇的查詢類別，可以是名稱、類型、地址等等。
     * @param query: 查詢條件。
     * @return 符合查詢條件的餐廳資訊，以List集合回傳。
     */
    List<Restaurant> getRestaurantByQuery(String type, String query);

    /**
     * 依id刪除指定餐廳
     * @param id: 欲刪除的餐廳id。
     */
    void deleteRestaurantById(Long id);

    List<Restaurant> getRestaurantByQueryApi(String nameQuery, String categoryQuery);
}
