package com.hfu.project_server.rest_controller;

import com.hfu.project_server.entity.Member;
import com.hfu.project_server.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<List<Member>> getMembers() {
        List<Member> members = memberService.getMembers();
        return ResponseEntity.ok().body(members);
    }

    @PostMapping("/members")
    public void registration(@RequestBody Member member) {
        memberService.registration(member);
    }


    // 測試 access denied
    @GetMapping("/access-denied")
    public void accessDenied() throws AccessDeniedException{
        throw new AccessDeniedException("no permission.");
    }
}
