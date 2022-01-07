package com.icia.member.dto;

import com.icia.member.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginDTO {
    private String memberEmail;
    private String memberPassword;

    public static MemberLoginDTO memberLoginDTO(MemberEntity member){
        MemberLoginDTO memberLoginDTO = new MemberLoginDTO();
        memberLoginDTO.setMemberEmail(member.getMemberEmail());
        memberLoginDTO.setMemberPassword(member.getMemberPassword());
        return memberLoginDTO;
    }
}


