package com.hotel.hotel_backend.security;


import com.hotel.hotel_backend.entity.UserEntity;
import com.hotel.hotel_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRol().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .build();
    }
}
