package com.hfu.project_server.service;

import com.hfu.project_server.entity.Member;

import java.util.List;

public interface MemberService {

    /**
     * 註冊功能
     * @param newMember: 新註冊會員
     */
    void registration(Member newMember);

    /**
     * 取得所有會員資料
     * @return 以<code>List<Member></></code>形式回傳的所有會員資訊。
     */
    List<Member> getMembers();

    /**
     * 取得無密碼之會員資訊。
     *
     * @param email: 欲查詢的會員email。
     * @return 不包含密碼資訊的Member物件。
     */
    Member getMemberDtoByEmail(String email);

    /**
     * 依Request Parameter查詢會員
     * @param type: 查詢類型。
     * @param query: 查詢條件。
     * @return 符合條件的List或是Member物件。
     */
    Object getAllOrByRequestParam(String type, String query);

    /**
     * 依id查詢會員
     *
     * @param id: 會員id。
     * @return 符合條件的Member物件。
     */
    Member getMemberById(Long id);

    /**
     * 修改會員的啟用狀態
     *
     * @param member: 欲修改的會員。
     * @param activation: 欲修改的啟用狀態。
     */
    void modifyActivation(Member member, boolean activation);
}
