package com.teamProject.ezmeal.dao;

import com.teamProject.ezmeal.domain.MemberDto;
import com.teamProject.ezmeal.service.LoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class MemberDaoTest {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private LoginService loginService;
    @Test
    public void getPassword() throws Exception {
        String kimjava100 = memberDao.getPassword("kimjava100");
        System.out.println("kimjava100 = " + kimjava100);
    }
    @Test
    public void checkNull() throws Exception {
        Long login = loginService.getLogin("kimjava100", "asdf1234");
        System.out.println("login = " + login);
    }

    @Test
    public void signupTest() throws Exception {
        MemberDto memberDto = new MemberDto("백자바","m","1995-08-15","010-1234-5678","java@naver.com","java","1234");
        assertTrue(memberDao.mbrSignup(memberDto) == 1);
        System.out.println("memberDto.getLgin_id() = " + memberDto.getLgin_id());
        System.out.println("memberDto.getName() = " + memberDto.getName());
    }

}