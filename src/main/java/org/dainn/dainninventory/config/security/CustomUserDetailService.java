package org.dainn.dainninventory.config.security;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.entity.UserEntity;
import org.dainn.dainninventory.repository.IRoleRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final IUserRepository userRepository;

    private final IRoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return null;
    }
    public UserDetails loadUserById(Integer id) throws UsernameNotFoundException {
        Optional<UserEntity> userEntities = userRepository.findById(id);
        if (userEntities.isPresent()) {
            UserEntity user = userEntities.get();
            user.setRoles(roleRepository.findByUsers(List.of(user)));
            return new CustomUserDetail(user);
        } else {
            return new CustomUserDetail(null, "", "", "", new ArrayList<>());
        }
    }

}
