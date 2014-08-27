package cn.timeoff.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.timeoff.security.core.DomainUserDetails;
import cn.timeoff.security.core.CurrentUser;

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
