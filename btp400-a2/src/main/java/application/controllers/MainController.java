package application.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import QRCode.QRCodeGenerator;
import classes.Bank;
import uk.oczadly.karl.jnano.model.NanoAmount;

@Controller
public class MainController {

	private volatile Bank bank = new Bank();
	private volatile int accountNo;

	@GetMapping("/")
	public String homePage(Model model) {
		model.addAttribute("group", "Group 4");
		accountNo = -1;
		return "home";
	}
	
	@RequestMapping(path = "/wallet", method = RequestMethod.GET)
	public String walletInfo(Model model) throws Exception {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username = "";

		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		
		if (accountNo == -1) {

			try (Connection connection = DriverManager.getConnection(
					"jdbc:postgresql://174.92.60.107:5432", "postgres",
					"123456");
					Statement statement = connection.createStatement()) {

				String query = "select accno from walletusers where username='" + username + "';";
				ResultSet queryResult = statement.executeQuery(query);

				if (queryResult.next()) {
					accountNo = queryResult.getInt("accno");
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		bank.updateBalance(accountNo);
		this.bank.getPubAddress(accountNo);
		String imageBase64 = QRCodeGenerator.generateQRCodeImage(this.bank.getPubAddress(accountNo), 250, 250);

		model.addAttribute("username", username);
		model.addAttribute("publicAddr", this.bank.getPubAddress(accountNo));
		model.addAttribute("accNo", accountNo);
		model.addAttribute("balAmt", this.bank.getBalance(accountNo));
		model.addAttribute("qrImg", "data:image/png;base64," + imageBase64);

		return "wallet";
	}

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public String send(Model model) {
		model.addAttribute("balAmt", this.bank.getBalance(accountNo));
		return "send";
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public String handleSend(Model model, @RequestParam int sendTo, @RequestParam double amount,
			RedirectAttributes redirAttrs) {
		bank.send(accountNo, sendTo, amount);
		return "redirect:/wallet";
	}

	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public String viewHistory(Model model) {
		model.addAttribute("UNIT_BAN", Bank.UNIT_BAN);
		model.addAttribute("history", bank.getAccountHistory(this.accountNo));
		return "history";
	}

}
