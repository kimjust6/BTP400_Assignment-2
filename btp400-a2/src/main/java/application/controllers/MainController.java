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

import QRCode.QRCodeGenerator;
import classes.Account;
import classes.Bank;

@Controller
public class MainController {

	@GetMapping("/")
	public String homePage(Model model) {
		model.addAttribute("group", "Group 4");
		return "home";
	}

	// could potentially improve this solution
	@GetMapping(path = "/wallet")
	public String walletInfo(Model model) throws Exception {
		Bank aBank = new Bank();

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username;

		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		try (Connection connection = DriverManager.getConnection(
				"jdbc:postgresql://ec2-54-209-43-223.compute-1.amazonaws.com:5432/d19rc88931g1bi", "luwrnpzmqrzvln",
				"9b76f4cfa5a87feb4cf28e8b90e485b183bca39b2b20c09e323b4a04b524b2ce");
				Statement statement = connection.createStatement()) {

			String query = "select accno from walletusers where username='" + username + "';";
			ResultSet queryResult = statement.executeQuery(query);

			if (queryResult.next()) {

				Account anAccount = aBank.getAccount(queryResult.getInt("accno"));
				String imageBase64 = QRCodeGenerator.generateQRCodeImage(anAccount.getPubAddress(), 250, 250);

				model.addAttribute("publicAddr", anAccount.getPubAddress());
				model.addAttribute("accNo", anAccount.getAccountNo());
				model.addAttribute("balAmt", anAccount.getBalance());
				model.addAttribute("qrImg", "data:image/png;base64," + imageBase64);
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "wallet";
	}

}