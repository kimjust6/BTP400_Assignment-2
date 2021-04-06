package com.example.btp400a2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import QRCode.QRCodeGenerator;
import bankingV20_0.Account;
import bankingV20_0.Bank;

@Controller
public class MainController {

	@GetMapping("/")
	public String homePage(Model model) {
		model.addAttribute("group", "Group 4");
		return "home";
	}

	@GetMapping(path = "/wallet/{walletID}")
	public String walletInfo(Model model, @PathVariable String walletID) throws Exception {
		Bank aBank = new Bank();
		Account anAccount = aBank.getAccount(Integer.parseInt(walletID));

		String imageBase64 = QRCodeGenerator.generateQRCodeImage(anAccount.getPubAddress(), 250, 250);
		
		// can be made into an attribute collection
		model.addAttribute("publicAddr", anAccount.getPubAddress());
		model.addAttribute("accNo", anAccount.getAccountNo());
		model.addAttribute("balAmt", anAccount.getBalance());
		model.addAttribute("qrImg", "data:image/png;base64," + imageBase64);

		return "wallet";
	}
	
	

	// this one isnt set up -------------------------------

	@GetMapping("/wallet")
	public String qrcode() throws Exception {
		return getQRCode();
	}

	public String getQRCode() throws Exception {
		Bank aBank = new Bank();
		Account anAccount = aBank.getAccount(0);

		String imageBase64 = QRCodeGenerator.generateQRCodeImage(anAccount.getPubAddress(), 250, 250);
		// imageBase64 =
		// QRCodeGenerator.generateQRCodeImage("991A38BED0D022D6622E9AD47513E2A14AC0DA58F15D8AFC81075DEC11CAF29D",250,250);
		return String.format("<div>Public Address: <a href=%s target=_blank>%s</a></div>",
				"https://creeper.banano.cc/explorer/account/" + anAccount.getPubAddress(), anAccount.getPubAddress())
				+ String.format("<div>Account Number: %s</div>", anAccount.getAccountNo())
				+ String.format("<div>Balance: %s</div>", anAccount.getBalance())
				+ String.format("<img src=\"%s\"></img>", "data:image/png;base64," + imageBase64);

	}

}