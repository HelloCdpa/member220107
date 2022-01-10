package com.icia.member.controller;

import com.icia.member.dto.MemberDetailDTO;
import com.icia.member.dto.MemberLoginDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.List;

import static com.icia.member.common.SessionConst.LOGIN_EMAIL;

@Controller
@RequestMapping("/member/*")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService ms;
    //회원가입 폼
    @GetMapping("save")
    public String saveForm(){
        return "member/save";
    }
    //회원가입
    @PostMapping("save")
    public String save(@ModelAttribute MemberSaveDTO memberSaveDTO){
        Long memberId = ms.save(memberSaveDTO);
        return "member/login";
    }
    //로그인 폼
    @GetMapping("login")
    public String loginForm(){
        return "member/login";
    }

    //로그인
    @PostMapping("login")
    public String login(@ModelAttribute MemberLoginDTO memberLoginDTO, HttpSession session){
        if(ms.login(memberLoginDTO)){
            session.setAttribute(LOGIN_EMAIL, memberLoginDTO.getMemberEmail());
//            return "redirect:/member/";
            return "member/mypage";
        } else {
            return "member/login";
        }
    }
    //회원 목록
    @GetMapping
    public String findAll(Model model){
        List<MemberDetailDTO> memberList = ms.findAll();
        model.addAttribute("memberList",memberList);
        return "member/findAll";
    }
    //회원조회 @PathVariable : url 변수를 넣어 줌
    @GetMapping("{memberId}")
    public String findById(@PathVariable("memberId") Long memberId,Model model){
        MemberDetailDTO member = ms.findById(memberId);
        //model 화면으로 데이터를 가져가는 방식
        model.addAttribute("member",member);
    return "member/findById";
    }
    //회원삭제
    @GetMapping("delete/{memberId}")
    public String deleteById(@PathVariable("memberId") Long memberId){
        ms.deleteById(memberId);
        return "redirect:/member/";
    }
    @PostMapping("{memberId}")
    public @ResponseBody MemberDetailDTO detail(@PathVariable long memberId){
        MemberDetailDTO member = ms.findById(memberId);
        return member;
    }

    //회원삭제 (/member/5)
    @DeleteMapping ("{memberId}")
    public ResponseEntity deleteById2(@PathVariable Long memberId){
        ms.deleteById(memberId);
        /*
         단순 화면 출력이 아닌 데이터를 리턴하고자 할 때 사용하는 리턴방식
         ResponseEntity : 데이터 &상태코드(200, 400, 404, 500 등)를 함께 리턴할 수 있음.
         @ResponseBody : 데이터만 리턴할 수 있음.
        */
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping ("update")
    public String updateForm(Model model,HttpSession session){
        // 세션값을 가져옴 Object > String 강제 형변환
        String memberEmail = (String) session.getAttribute(LOGIN_EMAIL);
        MemberDetailDTO member = ms.findByEmail(memberEmail);

        model.addAttribute("member",member);
        return "/member/update";
    }
    @PostMapping("update")
    public String update(@ModelAttribute MemberDetailDTO memberDetailDTO){
       Long memberId = ms.update(memberDetailDTO);
        //수정완료 후 해당회원의 상세페이지 출력
        return "redirect:/member/"+memberDetailDTO.getMemberId();
    }


    //수정처리 (put)
    @PutMapping("{memberId}")
    //json 으로 데이터가 전달되면 @RequestBody로 받아줘야함
    public ResponseEntity update2(@RequestBody MemberDetailDTO memberDetailDTO){
        Long memberId = ms.update(memberDetailDTO);
        return new ResponseEntity(HttpStatus.OK);
    }


}
