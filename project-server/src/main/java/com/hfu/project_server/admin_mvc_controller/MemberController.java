package com.hfu.project_server.admin_mvc_controller;

import com.hfu.project_server.entity.Member;
import com.hfu.project_server.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/members")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 會員列表，顯示或查詢會員資料
     */
    @GetMapping({"/", ""})
    public String showOrFindMembers(@RequestParam(value = "type", required = false) String type,
                                    @RequestParam(value = "query", required = false) String query,
                                    Model model) {

        Object members = memberService.getAllOrByRequestParam(type, query);
        model.addAttribute("members", members);

        return "member-list";
    }

    /**
     * TODO: 目前只能修改適用者的啟用狀態。
     *
     * 修改會員資料
     *
     * GET: 顯示修改頁面的表單。
     * POST: 提交修改。
     */
    @GetMapping("/update")
    public String update(@RequestParam(value = "memberId") Long id, Model model) {
        // get member by id
        Member memberById = memberService.getMemberById(id);
        model.addAttribute("member", memberById);
        return "update-member";
    }

    @PostMapping("/process-update")
    public String processUpdate(@ModelAttribute("member") Member member,
                                @RequestParam("activation") boolean modifiedActivation) {

        memberService.modifyActivation(member, modifiedActivation);
        return "redirect:/admin/members";
    }
}
