package com.hfu.project_server.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "member")
public class Member {

    public Member(Long id, String name, String email, String password, String imageUrl) {
        this(id, name, email, password, imageUrl, true);
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "m_id")
    private Long id;

    @Column(name = "m_name")
    private String name;

    @Column(name = "m_email")
    private String email;

    @Column(name = "m_password")
    private String password;

    @Column(name = "m_image_url")
    private String imageUrl;

    @Column(name = "m_active")
    private boolean isActive;
}
