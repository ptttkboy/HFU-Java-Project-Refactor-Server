package com.hfu.project_server.repository;

import com.hfu.project_server.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<List<Member>> findByNameContaining(String name);

    Optional<List<Member>> findByEmailContaining(String email);

    /**
     * 停用會員
     *
     * note: @Modifying註解是Spring Data JPA用於拓展@Query，以支援除了SELECT查詢以外的DML操作。
     * @param id: 欲停用之會員id
     */
    @Modifying
    @Query("update Member m set m.isActive=0 where m.id=?1")
    void deactivateMemberById(Long id);

    /**
     * 啟用會員
     *
     * @param id: 欲啟用之會員id
     */
    @Modifying
    @Query("update Member m set m.isActive=1 where m.id=?1")
    void activateMemberById(Long id);
}
