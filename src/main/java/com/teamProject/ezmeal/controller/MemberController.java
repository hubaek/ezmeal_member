package com.teamProject.ezmeal.controller;

import com.teamProject.ezmeal.domain.MemberDto;
import com.teamProject.ezmeal.service.LoginService;
import com.teamProject.ezmeal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;


    @PostMapping("/checkIdDuplicate")
    @ResponseBody
    public Map<String, Boolean> checkIdDuplicate(@RequestBody Map<String, String> request) {    // id중복체크 post JSON
        String id = request.get("id");  // input에 입력된 id의 value값 받아옴
        try {
            boolean isDuplicate = memberService.checkIdDuplicate(id);   // 기존 id가 존재하면 true
            Map<String, Boolean> response = new HashMap<>();
            response.put("isDuplicate", isDuplicate);   // map에 id를 조회한 결과를 담는다
            return response;    // JSON 응답을 보낸다.
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/checkEmailDuplicate")
    @ResponseBody
    public Map<String, Boolean> checkEmailDuplicate(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            boolean emailCheck = memberService.checkEmailDuplicate(email);
            Map<String, Boolean> response = new HashMap<>();
            response.put("emailCheck", emailCheck);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/signup")
    public String signUp() {    // 회원가입 버튼 클릭시 signup.jsp 화면 보여준다
        return "signup";
    }
    // PostMapping(/signup)
    // View success.jsp
    @PostMapping("/signup")
    public String signupSuccess(@Valid MemberDto memberDto, BindingResult bindingResult, String lgin_id, String lgin_pw ,
                                Model model, RedirectAttributes rattr, HttpServletRequest req) {
        // 1. 유효성 검사
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            for (ObjectError error : errors) {
                System.out.println(error.getDefaultMessage());
                model.addAttribute("errors", bindingResult);
                model.addAttribute("errorMsg", error.getDefaultMessage());
            }
            // 에러 메시지를 처리한 후에 다시 회원가입 페이지로 이동하거나, 필요한 로직을 수행합니다.
            return "signup";
        }

        // 2. DB에 신규회원 정보를 저장
        try {
            int rowCnt = memberService.signup(memberDto);    // insert

            if (rowCnt!=1) {  // insert가 되지않았을 때 signup페이지로 가도록 함
                rattr.addFlashAttribute("msg","회원가입이 되지 않았습니다. 다시 시도해주세요");
                return "redirect:/member/signup";
            }
            rattr.addFlashAttribute("msg","Signup_OK");
            // DB에 회원정보가 저장이 되고 동시에 로그인까지 되게 하려면,
            // memberId를 세션에 담는다.
            Long memberId = loginService.loginInfo(lgin_id);
            System.out.println("memberId = " + memberId);
            HttpSession session = req.getSession();
            session.setAttribute("memberId",memberId);
            MemberDto loginMbrInfo = memberService.mbrInfo(memberId);
            session.setAttribute("loginMbrInfo",loginMbrInfo);
            model.addAttribute("checkSignupSuccess", "signup success!!");
            return "signupSuccess"; // insert 성공시에 signupSuccess 페이지로 감
        } catch (Exception e) {
            e.printStackTrace();
//            rattr.addAttribute(memberDto);
            rattr.addFlashAttribute("msg","아이디가 이미 존재합니다. 회원가입을 다시 해주세요");
            return "redirect:/member/signup";    // 예외처리 발생시 signup(회원가입)페이지로 돌아감
        }
    }

//    private boolean isValid(MemberDto memberDto) {
//        return false;
//    }



}
