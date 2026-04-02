package com.xius.TarrifBuilder.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.xius.TarrifBuilder.Dao.LoginForm;
import com.xius.TarrifBuilder.Entity.ServicePlanPackMap;
import com.xius.TarrifBuilder.Entity.User;
import com.xius.TarrifBuilder.UserService.AuthService;
import com.xius.TarrifBuilder.UserService.ServicePlanService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private AuthService authService;
    @Autowired
    private ServicePlanService service;

    // Show login page
    @GetMapping("/loginform")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    // 🔥 CLEAN LOGIN METHOD
    @PostMapping("/login")
    public String processLogin(@ModelAttribute LoginForm loginForm,
            Model model, HttpSession session) {

        String role = loginForm.getRole();
        String network = loginForm.getNetworkName();
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("message", "Please enter Username");
            return "login";
        }
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("message", "Please enter Password");
            return "login";
        }

        if ("ADMIN".equalsIgnoreCase(role)) {
            if ("admin".equalsIgnoreCase(username) && "admin123".equals(password)) {
                session.setAttribute("username", "admin");
                return "redirect:/builder/admin";
            } else {
                model.addAttribute("message", "Invalid Admin Credentials");
                return "login";
            }
        }

        if (network == null || network.trim().isEmpty()) {
            model.addAttribute("message", "Please enter Network Name");
            return "login";
        }

        User user = authService.getUser(username, password, network);
        if (user == null) {
            model.addAttribute("message", "Invalid Username / Password / Network");
            return "login";
        }

        session.setAttribute("username", user.getLoginId());
        session.setAttribute("network", user.getNetwork().getNetworkDisplay());
        session.setAttribute("networkId", user.getNetwork().getNetworkId());

        return "redirect:/builder/step1";
    }

    // ── Step Pages ──

    @GetMapping("/builder/admin")
    public String admin(HttpSession session, Model model) {
        setCommonData(session, model);
        return "builder/admin";
    }

    @GetMapping("/builder/step1")
    public String step1(HttpSession session, Model model) {
        setCommonData(session, model);
        return "builder/step1";
    }

    @GetMapping("/builder/step2")
    public String step2(HttpSession session, Model model) {
        setCommonData(session, model);
        return "builder/step2";
    }

    @GetMapping("/builder/step2/filter")
    @ResponseBody
    public List<ServicePlanPackMap> getPlans(@RequestParam String types) {
        return service.getPlans(types);
    }

    @GetMapping("/builder/step3")
    public String step3(HttpSession session, Model model) {
        setCommonData(session, model);
        return "builder/step3";
    }

    @GetMapping("/builder/step4")
    public String step4(HttpSession session, Model model) {
        setCommonData(session, model);
        return "builder/step4";
    }

    @GetMapping("/builder/step5")
    public String step5(HttpSession session, Model model) {
        setCommonData(session, model);
        return "builder/step5";
    }

    // ✅ COMMON METHOD (CLEAN CODE)
    private void setCommonData(HttpSession session, Model model) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("network", session.getAttribute("network"));
        model.addAttribute("networkId", session.getAttribute("networkId"));
    }
}