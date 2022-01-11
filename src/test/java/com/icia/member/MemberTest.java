package com.icia.member;
import com.icia.member.dto.MemberDetailDTO;
import com.icia.member.dto.MemberLoginDTO;
import com.icia.member.dto.MemberMapperDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.repository.MemberMapperRepository;
import com.icia.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberTest {
    @Autowired
    private MemberService ms;

    @Autowired
    private MemberMapperRepository mmr;


    //unit test 하나씩 테스트
    @Test
    @Transactional //테스트 시작할 때 새로운 트랜잭션 시작
    @Rollback //테스트 종료 후 롤백 수행
    @DisplayName("로그인 테스트")
    public void loginTest(){
        final String email = "로그인용이메일";
        final String password = "로그인용비번";
        final String name = "로그인용이름";

        MemberSaveDTO memberSaveDTO = new MemberSaveDTO(email,password,name);
        ms.save(memberSaveDTO);

        MemberLoginDTO memberLoginDTO = new MemberLoginDTO(email,password);
        boolean loginResult = ms.login(memberLoginDTO);

        assertThat(loginResult).isEqualTo(true);


    }

    @Test
    @DisplayName("회원데이터생성")
    public void newMembers(){
        IntStream.rangeClosed(1, 15).forEach(i->{
            ms.save(new MemberSaveDTO("email"+i, "pw"+i, "name"+i));
        });
    }
    /*
    * 회원삭제 테스트코드를 만들어봅시다.
    * 회원삭제 시나리오를 작성해보고 코드도 짜보도록
    * */

    @Test
    @DisplayName("회원 데이터 삭제")
    @Transactional
    @Rollback
    public void deleteTest() {
        //삭제할 회원 생성
        final String email = "deleteEmail";
        final String password = "deletePw";
        final String name = "deleteName";

        MemberSaveDTO memberSaveDTO = new MemberSaveDTO(email,password,name);
        Long memberId = ms.save(memberSaveDTO);
        System.out.println(ms.findById(memberId));

        ms.deleteById(memberId);
        //java.util.NoSuchElementException: No value present 보여줄 값이 없음
        //삭제한 회원의 id 조회를 시도했을 때 null 이어야 테스트 통과
        /*System.out.println(ms.findById(memberId));*/

        /* 내가 한 거
        MemberLoginDTO memberLoginDTO = new MemberLoginDTO(email,password);
        boolean loginResult = ms.login(memberLoginDTO);

        assertThat(loginResult).isEqualTo(false);*/

        //NoSuchElementException은 무시하고 테스트를 수행
        // 예외를 던져버림 예외처리를 잘하는 사람이 젤 실력자
        assertThrows(NoSuchElementException.class, ()->{
            assertThat(ms.findById(memberId)).isNull(); // 삭제회원 id 조회결과가 null 이면 테스트 통과
        });

    }

    @Test
    @DisplayName("회원 데이터 업데이트")
    @Transactional
    @Rollback
    public void memberUpdateTest(){
        /*
        * 1. 신규회원 등록
        * 2. 신규회원에 대한 이름 수정
        * 3. 신규등록시 사용한 이름과 DB에 저장된 이름이 일치하는 지 판단
        * 4. 일치하지 않아야 테스트 통과
        */



        final String email = "updateEmail";
        final String password = "updatePw";
        final String name = "updateName";

        final String updateName = "changeName";

        MemberSaveDTO memberSaveDTO = new MemberSaveDTO(email,password,name);
        Long memberId = ms.save(memberSaveDTO);
        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        /*
        * 가입 후 DB에서 이름 조회
        * String saveMemberName = ms.findById(memberId);
        * MemberDetailDTO updateMember = new Member DetailDTO(memberId,email,password,updateName);
        * ms.update(updateMember);
        * 수정 후 DB에서 이름 조회
        * String updateMemberName = ms.findById(memberId);
        * assertThat(saveMemberName).isNotEqualTo(updateMemberName);
        * */

        memberDetailDTO.setMemberName(updateName);

        Long UpdateMemberId = ms.update(memberDetailDTO);

        assertThat(ms.findById(UpdateMemberId).getMemberName()).isEqualTo(updateName);
    }

    @Test
    @Transactional
    @DisplayName("mybatis 목록 출력 테스트")
    public void memberListTest(){
        List<MemberMapperDTO> memberList = mmr.memberList();
        for (MemberMapperDTO m:memberList){
            System.out.println("m.toString() = " + m);
        }
        List<MemberMapperDTO> memberList2 = mmr.memberList();
        for (MemberMapperDTO m:memberList2){
            System.out.println("m.toString() = " + m);
        }
    }

    @Test
    @DisplayName("mybatis 회원가입 테스트")
    public void memberSaveTest(){
        MemberMapperDTO memberMapperDTO = new MemberMapperDTO("회원이메일1","회원비번1","회원이름1");
        mmr.save(memberMapperDTO);
        MemberMapperDTO memberMapperDTO2 = new MemberMapperDTO("회원이메일2","회원비번2","회원이름2");
        mmr.save2(memberMapperDTO2);

        }
    }







