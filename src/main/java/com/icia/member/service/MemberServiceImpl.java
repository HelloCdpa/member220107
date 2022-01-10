package com.icia.member.service;

import com.icia.member.dto.MemberDetailDTO;
import com.icia.member.dto.MemberLoginDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.entity.MemberEntity;
import com.icia.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    //JpaRepository는 무조건 Entity만 받음
    private final MemberRepository mr;
    @Override
    public Long save(MemberSaveDTO memberSaveDTO) {
        MemberEntity memberEntity = MemberEntity.saveMember(memberSaveDTO);
        System.out.println("MemberServiceImpl.save");
        return mr.save(memberEntity).getId();
    }

    @Override
    public boolean login(MemberLoginDTO memberLoginDTO) {
        MemberEntity memberEntity = mr.findByMemberEmail(memberLoginDTO.getMemberEmail());
        if (memberEntity != null){
            if(memberLoginDTO.getMemberPassword().equals(memberEntity.getMemberPassword())){
                return true;
            }else{
            return false;
            }
        }else {
            return false;
        }

    }

    @Override
    public List<MemberDetailDTO> findAll() {
        List<MemberEntity> memberEntityList = mr.findAll();
        List<MemberDetailDTO> memberList = new ArrayList<>();
        for(MemberEntity m:memberEntityList){
            memberList.add(MemberDetailDTO.toMemberDetailDTO(m));
        }
        return memberList;

    }

    @Override
    public MemberDetailDTO findById(Long memberId) {
        //널포인트에러 방지
        Optional<MemberEntity> memberEntityOptional = mr.findById(memberId);
        MemberEntity memberEntity = memberEntityOptional.get();
        MemberDetailDTO memberDetailDTO = MemberDetailDTO.toMemberDetailDTO(memberEntity);
        return memberDetailDTO;
        //return MemberDetailDTO.toMemberDetailDTO(mr.findById(memberId).get());
    }

    @Override
    public void deleteById(Long memberId) {
        mr.deleteById(memberId);
    }

    @Override
    public Long update(MemberDetailDTO memberDetailDTO) {
        //update 처리시 save 메서드 호출 동일한 아이디가 있으면 그냥 덮어쓰기 해버림
        //MemberDetailDTO -> MemberEntity 대상에 메서드가 있어야 함.
        //detail -> entity 로 변환시키기
        MemberEntity memberEntity = MemberEntity.toUpdateMember(memberDetailDTO);
        Long memberId = mr.save(memberEntity).getId();
        return memberId;
    }

    @Override
    public MemberDetailDTO findByEmail(String memberEmail) {
        MemberEntity memberEntity = mr.findByMemberEmail(memberEmail);
        MemberDetailDTO memberDetailDTO = MemberDetailDTO.toMemberDetailDTO(memberEntity);
        //서비스에서 나갈수 있는것은 DTO 만!!!!

        return memberDetailDTO;
    }
}
