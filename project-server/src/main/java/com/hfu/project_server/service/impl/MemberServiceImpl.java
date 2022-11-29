package com.hfu.project_server.service.impl;

import com.google.common.base.Strings;
import com.hfu.project_server.entity.Member;
import com.hfu.project_server.exception.EmailAlreadyUsedException;
import com.hfu.project_server.repository.MemberRepository;
import com.hfu.project_server.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 註冊會員
     * @param member
     * @return 成功：200，不回傳結果；信箱已被註冊：409，丟出EmailAlreadyUsedException。
     */
    @Override
    public void registration(Member member) {

        // TODO: 待改善 應新增validation機制檢核送來的json資料)

        String email = member.getEmail();

        if(memberRepository.findByEmail(email).isPresent()) {
            log.info("Registration Failed: Email has already been used: {}", email);
            throw new EmailAlreadyUsedException(String.format("Email: %s has already been used.", email));
        }

        log.info("Registration Success, member name: {}", member.getName());

        // encode
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        // 預設帳號啟用
        member.setActive(true);
        memberRepository.save(member);
    }

    /**
     * 取得所有會員
     *
     * @return List<Member>
     */
    @Override
    public List<Member> getMembers() {
        log.info("Get all members.");
        return memberRepository.findAll();
    }

    @Override
    public Member getMemberDtoByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(m -> new Member(m.getId(), m.getName(), m.getEmail(), null, m.getImageUrl()))
                .orElse(null);
    }

    /**
     * 依照RequestParam來尋找符合條件的會員，如沒有輸入(或條件輸入不完整)，則預設回傳所有會員。
     *
     * @param type：表示依照類型查詢，包含名稱(name)，信箱(email)，編號(id)。
     * @param query:表示查詢的內容。
     * @return List形式的Member，若查無結果則回傳空的List。
     */
    @Override
    public Object getAllOrByRequestParam(String type, String query) {

        if (Strings.isNullOrEmpty(type) || Strings.isNullOrEmpty(query)) {
            return memberRepository.findAll();
        }

        switch (type) {

            case "email" :
                return memberRepository.findByEmailContaining(query).orElse(null);
            case "name" :
                return memberRepository.findByNameContaining(query).orElse(null);
            case "id" :
                return memberRepository.findById(Long.valueOf(query)).orElse(null);
            default:
                return null;
        }
    }

    @Override
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }


    /**
     * 修改使用者的啟用狀態
     *
     * 比較回傳修改後的activation狀態和原本的狀態，
     * 若修改後的啟用狀態和原本的相同，視為不更改；
     * 若和原本的不一樣，依情況調用"啟用 activateMember"或“停用 deactivateMember”。
     *
     * @param member
     * @param modifiedActivation
     */
    @Override
    public void modifyActivation(Member member, boolean modifiedActivation) {

        Long id = member.getId();
        boolean currentActivation = memberRepository.findById(id).get().isActive();

        // 原來的啟用狀態和修改後的狀態一樣就不更新
        if (currentActivation == modifiedActivation) {
            return;
        }

        if (modifiedActivation) {
            memberRepository.activateMemberById(id);
            log.info("Activate member");
        } else {
            memberRepository.deactivateMemberById(id);
            log.info("Deactivate member");
        }
    }
}
