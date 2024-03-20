package io.devridge.api.config.auth;


import io.devridge.api.handler.ex.NotFoundUserByEmailException;
import io.devridge.api.repository.user.ApiUserRepository;
import io.devridge.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class StandardUserService implements UserDetailsService {

    private final ApiUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundUserByEmailException(email));

        return LoginUser.builder().user(user).build();
    }
}
