package com.icia.member.controller;

import com.icia.member.dto.MemberDetailDTO;
import com.icia.member.dto.MemberLoginDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.service.MemberService;
import lombok.RequiredArgsConstructor;
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
    @GetMapping("login")
    public String loginForm(){
        return "member/login";
    }
    @PostMapping("login")
    public String login(@ModelAttribute MemberLoginDTO memberLoginDTO, HttpSession session){
        if(ms.login(memberLoginDTO)){
            session.setAttribute(LOGIN_EMAIL, memberLoginDTO.getMemberEmail());
            return "redirect:member/findAll";
        }

        return "member/login";
    }
    //회원 목록
    @GetMapping
    public String findAll(Model model){
        List<MemberDetailDTO> memberList = ms.findAll();
        model.addAttribute("memberList",memberList);
        return "member/findAll";
    }
    @GetMapping("{memberId}")
    public String findById(@PathVariable("memberId") Long memberId,Model model){
        MemberDetailDTO member = ms.findById(memberId);
        model.addAttribute("member",member);
    return "member/findById";
    }









}
