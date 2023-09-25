package io.devridge.api.config.auth;


import io.devridge.api.domain.user.User;
import io.devridge.api.domain.user.UserRepository;
import io.devridge.api.handler.ex.NotFoundUserByEmailException;
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

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundUserByEmailException(email));

        return LoginUser.builder().user(user).build();
    }
}
