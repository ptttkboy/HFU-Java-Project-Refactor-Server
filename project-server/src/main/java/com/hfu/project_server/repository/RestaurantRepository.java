package com.hfu.project_server.repository;

import com.hfu.project_server.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<List<Restaurant>> findTop5ByOrderByIdDesc();

    Optional<List<Restaurant>> findByNameContainingIgnoreCase(String name);

    Optional<List<Restaurant>> findByCategory(String category);

    Optional<List<Restaurant>> findByAddressContainingIgnoreCase(String address);
}
