package com.hfu.project_server.security.member_web_api;

import com.hfu.project_server.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MemberUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        MemberUserDetail memberUserDetail = memberRepository.findByEmail(email)
                .map(member -> new MemberUserDetail(
                        member.getEmail(),
                        member.getPassword(),
                        null,
                        true,
                        true,
                        true,
                        member.isActive()))
                .orElseThrow(() -> {
                    log.error("User: %s not found", email);
                    return new UsernameNotFoundException(String.format("User: %s not found", email));
                });
        return memberUserDetail;
    }

//    private void checkEnable(UserDetails userDetails) {
//
//        if(!userDetails.isEnabled()) {
//            log.warn("User: {} is deactivated.", userDetails.getUsername());
//            throw new MemberNotActivate("使用者已被停用，請聯絡管理員");
//        }
//    }
}
