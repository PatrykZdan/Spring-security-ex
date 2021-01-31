package com.example.springbootsec;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.security.core.userdetails.User.withUsername;

@Component
public class UserRepositoryUserDetailService implements UserDetailsService {

    private final UserRepository users;
    private final JwtProvider jwtProvider;

    public UserRepositoryUserDetailService(UserRepository users, JwtProvider jwtProvider) {
        this.users = users;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.findByUsername(username)
                .map(BridgeUser::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<UserDetails> loadUserByJwtToken(String jwtToken) {
        if (jwtProvider.isValidToken(jwtToken)) {
            return Optional.of(
                    withUsername(jwtProvider.getUsername(jwtToken))
                            .authorities(jwtProvider.getRoles(jwtToken))
                            .password("") //token does not have password but field may not be empty
                            .accountExpired(false)
                            .accountLocked(false)
                            .credentialsExpired(false)
                            .disabled(false)
                            .build());
        }
        return Optional.empty();
    }

    private static class BridgeUser extends User implements UserDetails {

        public BridgeUser(User user) {
            super(user);
        }


        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return userAuthorities.stream()
                    .map(userAuthority -> new SimpleGrantedAuthority(userAuthority.authority))
                    .collect(Collectors.toSet());
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }
    }
}
