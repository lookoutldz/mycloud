package org.looko.mycloud.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.commonstarter.dto.UserAuthDTO;
import org.looko.mycloud.gateway.feignclient.UserAuthRestClient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class AuthorizationService implements ReactiveUserDetailsService {

    private final UserAuthRestClient userAuthRestClient;

    public AuthorizationService(UserAuthRestClient userAuthRestClient) {
        this.userAuthRestClient = userAuthRestClient;
    }

    @Override
    public Mono<UserDetails> findByUsername(String usernameOrEmail) {
        CompletableFuture<UserAuthDTO> future =
                CompletableFuture.supplyAsync(() -> userAuthRestClient.getUserByUsernameOrEmail(usernameOrEmail));

        return Mono.fromFuture(future)
                .map(this::toUserDetails)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("用户名或密码错误")));
    }

    private UserDetails toUserDetails(UserAuthDTO userAuthDTO) {
        String username = userAuthDTO.username();
        String password = userAuthDTO.password();
        List<GrantedAuthority> authorities = getAuthorities(List.of("user"));
        return User.builder()
                .username(username)
                .password(password)
                .authorities(authorities)
                .build();
    }

    private List<GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
