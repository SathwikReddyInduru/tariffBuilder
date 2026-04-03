package com.xius.TariffBuilder.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xius.TariffBuilder.Dao.LoginForm;
import com.xius.TariffBuilder.Dao.TariffDAO;
import com.xius.TariffBuilder.Entity.ServicePlanPackMap;
import com.xius.TariffBuilder.Entity.User;
import com.xius.TariffBuilder.UserService.AuthService;
import com.xius.TariffBuilder.UserService.ServicePlanService;
import com.xius.TariffBuilder.UserService.TariffService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BuilderController {
	@Autowired
	private AuthService authService;
	//
	@Autowired
	private ServicePlanService service;

	@Autowired
	private TariffService tariffService;

	// ✅ Show Login Page
	@GetMapping("/loginform")
	public String showLoginForm(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "login";
	}

	@PostMapping("/login")
	public String processLogin(@ModelAttribute LoginForm loginForm,
			Model model,
			HttpSession session) {

		String role = loginForm.getRole();
		String username = loginForm.getUsername();
		String password = loginForm.getPassword();
		String network = loginForm.getNetworkName();

		// ================= ADMIN LOGIN =================
		if ("ADMIN".equalsIgnoreCase(role)) {

			// validation
			if (username == null || username.trim().isEmpty()) {

				model.addAttribute("message", "Please enter Admin Username");
				model.addAttribute("role", "ADMIN");
				model.addAttribute("loginForm", loginForm);

				return "login";
			}

			if (password == null || password.trim().isEmpty()) {

				model.addAttribute("message", "Please enter Admin Password");
				model.addAttribute("role", "ADMIN");
				model.addAttribute("loginForm", loginForm);

				return "login";
			}

			// correct credentials
			if ("admin".equalsIgnoreCase(username) && "admin123".equals(password)) {

				session.setAttribute("username", "admin");

				return "redirect:/builder/admin";
			}

			// wrong credentials
			model.addAttribute("message", "Invalid Admin Credentials");

			model.addAttribute("role", "ADMIN"); // keep toggle ON
			model.addAttribute("loginForm", loginForm); // keep entered values

			return "login"; // SAME PAGE (no redirect)
		}

		// ================= USER LOGIN =================
		if (network == null || network.trim().isEmpty()) {
			model.addAttribute("message", "Please enter Network Name");
			model.addAttribute("role", "USER");
			return "login";
		}

		if (username == null || username.trim().isEmpty()) {
			model.addAttribute("message", "Please enter Username");
			model.addAttribute("role", "USER");
			return "login";
		}

		if (password == null || password.trim().isEmpty()) {
			model.addAttribute("message", "Please enter Password");
			model.addAttribute("role", "USER");
			return "login";
		}

		User user = authService.getUser(username, password, network);

		if (user == null) {
			model.addAttribute("message", "Invalid Username / Password / Network");
			model.addAttribute("role", "USER");
			return "login";
		}

		session.setAttribute("username", user.getLoginId());
		session.setAttribute("network", user.getNetwork().getNetworkDisplay());
		session.setAttribute("networkId", user.getNetwork().getNetworkId());

		return "redirect:/builder/step1";
	}

	@GetMapping("/builder/admin")
	public String adminPage(HttpSession session, Model model) {
		setCommonData(session, model);
		List<TariffDAO> tariffList = tariffService.getTariffPackages();
		model.addAttribute("tariff", tariffList); // "tariff" matches ${tariff} in HTML
		return "builder/admin";
	}

	// @GetMapping("/builder/admin")
	// public String adminPage(HttpSession session, Model model) {
	// setCommonData(session, model);
	// return "builder/admin"; // must be in templates/builder/
	// }

	@GetMapping("/builder/admin1")
	public String adminPage_pendingtarrif(HttpSession session, Model model) {
		setCommonData(session, model);
		model.addAttribute("tariff", tariffService.getPendingTariffs()); // ← use this, not getTariffPackages()
		return "builder/admin";
	}

	@PostMapping("/admin/updateStatus")
	@ResponseBody
	public ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> body) {
		Long networkId = Long.valueOf(body.get("networkId").toString());
		String status = body.get("status").toString();
		tariffService.updateStatus(networkId, status);
		return ResponseEntity.ok("success");
	}
	// ── Step Pages ──

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
	public List<Map<String, Object>> getPlans(@RequestParam String types,
			HttpSession session) {

		Long networkId = (Long) session.getAttribute("networkId");

		List<ServicePlanPackMap> plans = service.getPlans(types);

		return plans.stream().map(p -> {
			Map<String, Object> map = new HashMap<>();
			map.put("servicePackageId", p.getServicePackageId());
			map.put("servicePackageName", p.getServicePackageName());
			return map;
		}).collect(Collectors.toList());
	}

	@GetMapping("/builder/step3")
	public String step3(HttpSession session, Model model) {
		setCommonData(session, model);
		return "builder/step3";
	}

	@GetMapping("/builder/step3/filter")
    @ResponseBody
    public List<ServicePlanPackMap> getDAtpPlans(@RequestParam String types) {
    	return service.getDAtpPlans(types);
    }

	@GetMapping("/builder/step4")
	public String step4(HttpSession session, Model model) {
		setCommonData(session, model);
		return "builder/step4";
	}
	
	@GetMapping("/builder/step4/filter")
    @ResponseBody
    public List<ServicePlanPackMap> getAAtpPlans(@RequestParam String types) {
    	return service.getAAtpPlans(types);
    }

	@GetMapping("/builder/step5")
	public String step5(HttpSession session, Model model) {
		setCommonData(session, model);
		return "builder/step5";
	}

	// ✅ COMMON METHOD
	private void setCommonData(HttpSession session, Model model) {
		model.addAttribute("username", session.getAttribute("username"));
		model.addAttribute("network", session.getAttribute("network"));
		model.addAttribute("networkId", session.getAttribute("networkId"));
	}

	@GetMapping("/tariffPackages")
	@ResponseBody
	public List<TariffDAO> getTariffPackages() {

		return tariffService.getTariffPackages();
	}

}
