package org.dainn.dainninventory.service.security;

import org.dainn.dainninventory.entity.UserEntity;
import org.dainn.dainninventory.repository.IRoleRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.utils.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
//@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userEntities = userRepository.findByEmailAndStatus(email, 1);
        if (userEntities.isPresent()) {
            UserEntity user = userEntities.get();
            user.setRoles(roleRepository.findByUsers(List.of(user)));
            return new CustomUserDetail(user);
        } else {
            return new CustomUserDetail(email, "", "", new ArrayList<>());
        }
    }
    public UserDetails loadUserByUsernameAndProviderId(String email, Provider provider) throws UsernameNotFoundException {
        Optional<UserEntity> userEntities = userRepository.findByEmailAndProvider(email, provider);
        if (userEntities.isPresent()) {
            UserEntity user = userEntities.get();
            user.setRoles(roleRepository.findByUsers(List.of(user)));
            return new CustomUserDetail(user);
        } else {
            return new CustomUserDetail(email, "", "", new ArrayList<>());
        }
    }
}
