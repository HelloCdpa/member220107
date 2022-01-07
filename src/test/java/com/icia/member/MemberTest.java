package com.icia.member;
import com.icia.member.dto.MemberLoginDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberTest {
    @Autowired
    private MemberService ms;


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












}
