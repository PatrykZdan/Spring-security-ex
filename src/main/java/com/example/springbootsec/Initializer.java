package com.example.springbootsec;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Initializer  implements SmartInitializingSingleton {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public Initializer(UserRepository repository,
                       PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void afterSingletonsInstantiated() {
        String secretAuthority = "SECRET";
        String secretAuthority2 = "SECRET2";
        UUID joshId = UUID.fromString("219168d2-1da4-4f8a-85d8-95b4377af3c1");
        UUID carolId = UUID.fromString("328167d1-2da3-5f7a-86d7-96b4376af3c0");
        User josh = new User(joshId, "josh", encoder.encode("josh"));
        User carol = new User(carolId, "carol", encoder.encode("carol"));
        josh.addAuthority(secretAuthority);
        josh.addAuthority(secretAuthority2);
        carol.addAuthority(secretAuthority);

        repository.save(josh);
        repository.save(carol);


    }
}
