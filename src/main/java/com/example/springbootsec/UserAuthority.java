package com.example.springbootsec;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity(name = "authorities")
public class UserAuthority {

    @Id
    UUID id;

    @ManyToOne
    User user;

    String authority;

    public UserAuthority(User user, String authority) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.authority = authority;
    }

    public UserAuthority() {
    }
}
