package com.hfu.project_server.security.admin_mvc;

import com.hfu.project_server.repository.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 管理者的UserDetailService
 *
 * 提供UserDetails給Provider驗證流程使用。
 */
@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    /**
     * 取出UserDetails
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByUsername(username)
                .map(admin -> new AdminUserDetails(
                        admin.getUsername(),
                        admin.getPassword(),
                        true,
                        true,
                        true,
                        true))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Admin not found. Username: %s", username)));
    }
}
