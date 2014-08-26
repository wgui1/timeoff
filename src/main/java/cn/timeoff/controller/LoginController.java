package cn.timeoff.controller;

import java.util.ArrayList;
import java.util.Arrays;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.timeoff.security.core.DomainUserDetails;
import cn.timeoff.security.core.DomainUserDetailsImpl;
import cn.timeoff.security.core.CurrentUser;
import cn.timeoff.security.service.DomainUserDetailsManager;

@Controller
public interface LoginController {

    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    String getLoginPage(Model model);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    String getLoginPage(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model);

    @RequestMapping(value="/myaccount", method=RequestMethod.GET)
    String userDetails(@CurrentUser DomainUserDetails userDetails, Model model);

    @RequestMapping(value="/register", method=RequestMethod.GET)
    String new_form();
    
    @RequestMapping(value="/register", method=RequestMethod.POST)
    String createCooperation(@RequestParam("cooperation") String domainName,
                         @RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("email") String email,
                         Model model);
}
