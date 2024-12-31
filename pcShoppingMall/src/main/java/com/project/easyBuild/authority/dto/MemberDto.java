package com.project.easyBuild.authority.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private int memberNo;          // 작성 번호
    private String userId;         // 사용자 ID
    private int authId;            // 권한 ID
    private String password;       // 비밀번호
    private String username;       // 사용자 이름
    private String gender;         // 성별
    private String email;          // 이메일
    private String phone;          // 전화번호
    private LocalDateTime registerDate; // 등록일
    private LocalDateTime lastUpdate;   // 마지막 업데이트
    private int purchaseCount;     // 구매 횟수
    private String memberStatus;   // 회원 상태
}
