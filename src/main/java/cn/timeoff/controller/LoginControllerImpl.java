package cn.timeoff.controller;

import java.util.ArrayList;
import java.util.Arrays;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import cn.timeoff.security.core.DomainUserDetails;
import cn.timeoff.security.core.DomainUserDetailsImpl;
import cn.timeoff.security.core.CurrentUser;
import cn.timeoff.security.service.DomainUserDetailsManager;

@Component
@Transactional
public class LoginControllerImpl implements LoginController{

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DomainUserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String getLoginPage(Model model) {
        return "login";
    }

    public String getLoginPage(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model) {
        return "redirect:myaccount";
    }

    public String userDetails(@CurrentUser DomainUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails);
        return "myaccount";
    }

    public String new_form() {
        return "register";
    }

    public String createCooperation(@RequestParam("cooperation") String domainName,
                         @RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("email") String email,
                         Model model) {
        String encoded_pass = passwordEncoder.encode(password);
        DomainUserDetails user = new DomainUserDetailsImpl(domainName,
                username, encoded_pass, email, true, true, true, true,
                new ArrayList<SimpleGrantedAuthority>());
        userDetailsManager.createDomain(domainName);
        userDetailsManager.createUser(user);
        userDetailsManager.createGroup(domainName, "ADMIN",
                                       Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
        userDetailsManager.addUserToGroup(domainName, "ADMIN", username);
        return "redirect:myaccount";
    }
}
