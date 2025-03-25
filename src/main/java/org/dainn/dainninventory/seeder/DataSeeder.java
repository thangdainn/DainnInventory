package org.dainn.dainninventory.seeder;

import lombok.RequiredArgsConstructor;
import org.dainn.dainninventory.entity.RoleEntity;
import org.dainn.dainninventory.entity.UserEntity;
import org.dainn.dainninventory.repository.IRoleRepository;
import org.dainn.dainninventory.repository.IUserRepository;
import org.dainn.dainninventory.utils.constant.RoleConstant;
import org.dainn.dainninventory.utils.enums.Provider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            RoleEntity adminRole = new RoleEntity();
            adminRole.setName(RoleConstant.PREFIX_ROLE + "ADMIN");
            RoleEntity staffRole = new RoleEntity();
            staffRole.setName(RoleConstant.PREFIX_ROLE + "STAFF");
            RoleEntity userRole = new RoleEntity();
            userRole.setName(RoleConstant.PREFIX_ROLE + "USER");
            roleRepository.saveAll(List.of(adminRole, staffRole, userRole));

            if (userRepository.count() == 0) {
                UserEntity admin = new UserEntity();
                admin.setEmail("admin@gmail.com");
                admin.setName("Administrator");
                admin.setPhone("0333207334");
                admin.setPassword(encoder.encode("123123"));
                admin.setProvider(Provider.local);
                admin.setRole(adminRole);
                userRepository.save(admin);
            }
        }
    }
}
