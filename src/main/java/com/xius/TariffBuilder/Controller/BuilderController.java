package com.xius.TariffBuilder.Controller;

import java.util.List;
import java.util.Map;

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
import com.xius.TariffBuilder.Dao.SavePackageRequest;
import com.xius.TariffBuilder.Dao.TariffDao;
import com.xius.TariffBuilder.Entity.ServicePlanPackMap;
import com.xius.TariffBuilder.Entity.User;
import com.xius.TariffBuilder.UserService.AuthService;
import com.xius.TariffBuilder.UserService.ServicePlanService;
import com.xius.TariffBuilder.UserService.TariffSaveService;
import com.xius.TariffBuilder.UserService.TariffService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BuilderController {
	@Autowired
	private AuthService authService;

	@Autowired
	private ServicePlanService service;

	@Autowired
	private TariffService tariffService;

	@Autowired
	private TariffSaveService tariffSaveService;

	// Show Login Page
	@GetMapping("/loginform")
	public String showLoginForm(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "login";
	}

	@PostMapping("/login")
	public String processLogin(@ModelAttribute LoginForm loginForm,
			Model model,
			HttpSession session) {

		String username = loginForm.getUsername();
		String password = loginForm.getPassword();
		String network = loginForm.getNetworkName();

		// ================= USER LOGIN =================
		if (network == null || network.trim().isEmpty()) {
			model.addAttribute("message", "Please enter Network Name");
			model.addAttribute("loginForm", loginForm);
			return "login";
		}

		if (username == null || username.trim().isEmpty()) {
			model.addAttribute("message", "Please enter Username");
			model.addAttribute("loginForm", loginForm);
			return "login";
		}

		if (password == null || password.trim().isEmpty()) {
			model.addAttribute("message", "Please enter Password");
			model.addAttribute("loginForm", loginForm);
			return "login";
		}

		User user = authService.getUser(username, password, network);

		if (user == null) {
			model.addAttribute("message", "Invalid Username / Password / Network");
			model.addAttribute("loginForm", loginForm);
			return "login";
		}

		session.setAttribute("username", user.getLoginId());
		session.setAttribute("network", user.getNetwork().getNetworkDisplay());
		session.setAttribute("networkId", user.getNetwork().getNetworkId());

		return "redirect:/builder/step1";
	}

	@GetMapping("/builder/admin")
	public String adminPage(HttpSession session, Model model) {

		if (isNotLoggedIn(session))
			return "redirect:/loginform";

		setCommonData(session, model);
		List<TariffDao> tariffList = tariffService.getTariffPackages();
		model.addAttribute("tariff", tariffList);

		return "builder/admin";
	}

	@GetMapping("/builder/pendingtariff")
	public String adminPage_pendingtarrif(HttpSession session, Model model) {

		if (isNotLoggedIn(session))
			return "redirect:/loginform";

		setCommonData(session, model);
		model.addAttribute("tariff", tariffService.getPendingTariffs());
		return "builder/admin";
	}

	@PostMapping(value = "/admin/updateStatus", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<String> updateStatus(@RequestBody TariffDao req) {
		tariffService.updateStatus(req.getTariffPackageId(), req.getStatus());
		return ResponseEntity.ok("success");
	}

	// ── Step Pages ──

	@GetMapping("/builder/step1")
	public String step1(HttpSession session, Model model) {

		if (isNotLoggedIn(session))
			return "redirect:/loginform";

		setCommonData(session, model);
		return "builder/step1";
	}

	@GetMapping("/builder/step2")
	public String step2(HttpSession session, Model model) {

		if (isNotLoggedIn(session))
			return "redirect:/loginform";

		setCommonData(session, model);
		return "builder/step2";
	}

	@GetMapping("/builder/step2/filter")
	@ResponseBody
	public List<ServicePlanPackMap> getTpPlans(@RequestParam String types, HttpSession session) {
		Long networkId = (Long) session.getAttribute("networkId");
		return service.getTpPlans(networkId, types);
	}

	@GetMapping("/builder/step3")
	public String step3(HttpSession session, Model model) {

		if (isNotLoggedIn(session))
			return "redirect:/loginform";

		setCommonData(session, model);
		return "builder/step3";
	}

	@GetMapping("/builder/step3/filter")
	@ResponseBody
	public List<ServicePlanPackMap> getDAtpPlans(@RequestParam String types, HttpSession session) {
		Long networkId = (Long) session.getAttribute("networkId");
		return service.getDAtpPlans(networkId, types);
	}

	@GetMapping("/builder/step4")
	public String step4(HttpSession session, Model model) {

		if (isNotLoggedIn(session))
			return "redirect:/loginform";

		setCommonData(session, model);
		return "builder/step4";
	}

	@GetMapping("/builder/step4/filter")
	@ResponseBody
	public List<ServicePlanPackMap> getAAtpPlans(@RequestParam String types, HttpSession session) {
		Long networkId = (Long) session.getAttribute("networkId");
		return service.getAAtpPlans(networkId, types);
	}

	@GetMapping("/builder/step5")
	public String step5(HttpSession session, Model model) {

		if (isNotLoggedIn(session))
			return "redirect:/loginform";

		setCommonData(session, model);
		return "builder/step5";
	}

	@GetMapping("/tariffPackages")
	@ResponseBody
	public List<TariffDao> getTariffPackages() {
		return tariffService.getTariffPackages();
	}

	@PostMapping("/saveConfig")
	@ResponseBody
	public ResponseEntity<?> saveConfig(@RequestBody SavePackageRequest request) {
		Long id = tariffSaveService.savePackage(
				request,
				request.getNetworkId(),
				request.getUsername());
		return ResponseEntity.ok(Map.of("success", true, "packageId", id));
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/loginform";
	}

	// COMMON METHOD
	private void setCommonData(HttpSession session, Model model) {
		model.addAttribute("username", session.getAttribute("username"));
		model.addAttribute("network", session.getAttribute("network"));
		model.addAttribute("networkId", session.getAttribute("networkId"));
	}

	private boolean isNotLoggedIn(HttpSession session) {
		return session.getAttribute("username") == null;
	}
}