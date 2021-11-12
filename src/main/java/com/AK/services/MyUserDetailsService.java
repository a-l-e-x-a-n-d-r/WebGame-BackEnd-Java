package com.AK.services;

import com.AK.exceptions.userExceptions.userLoginExceptions.UserNotFoundException;
import com.AK.models.CustomUserDetails;
import com.AK.models.User;
import com.AK.repositories.UserRepository;
import java.util.Optional;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public MyUserDetailsService(
      UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @SneakyThrows
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepository.findByUsername(username);

    if (optionalUser.isEmpty()) {
      throw new UserNotFoundException("No user found.");
    }
    User user = optionalUser.get();
    return new CustomUserDetails(user);
  }
}
