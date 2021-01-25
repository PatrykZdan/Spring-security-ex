package com.example.springbootsec;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "users")
public class User {

    @Id
    UUID id;

    @Column(name = "username", unique = true)
    String username;

    String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<UserAuthority> userAuthorities = new ArrayList<>();

    Boolean enabled = true;

    public User(UUID id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(User user) {
        this.id = user.id;
        this.username = user.username;
        this.password = user.password;
        this.userAuthorities = user.userAuthorities;
        this.enabled = user.enabled;
    }

    public User() {
    }

    public void addAuthority(String authority) {
        userAuthorities.add(new UserAuthority(this, authority));
    }
}
