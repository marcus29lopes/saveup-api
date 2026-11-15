package com.saveup.api.user.service;

import com.saveup.api.user.entity.ApiUser;
import com.saveup.api.user.repository.ApiUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//UserDetailsService trazer detalhes de usuario dO DB
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final ApiUserRepository apiUserRepository;

    public CustomUserDetailsService(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ApiUser user = apiUserRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
        return User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .disabled(!user.isEnabled())
                .authorities(authorities)
                .build();
    }
}
