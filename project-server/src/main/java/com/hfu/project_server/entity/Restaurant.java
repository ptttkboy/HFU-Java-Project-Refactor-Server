package com.hfu.project_server.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "r_id")
    private Long id;

    @NotEmpty(message = "必要的資訊")
    @Column(name = "r_name")
    private String name;

    @NotEmpty(message = "必要的資訊")
    @Column(name = "r_category")
    private String category;

    @NotEmpty(message = "必要的資訊")
    @Column(name = "r_address")
    private String address;

    @Column(name = "r_phone")
    private String phone;

    @Column(name = "r_description")
    private String description;

    @Column(name = "r_image_url")
    private String imageUrl;

}
