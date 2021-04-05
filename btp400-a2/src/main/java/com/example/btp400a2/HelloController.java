package com.example.btp400a2;

 
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import QRCode.QRCodeGenerator;
import bankingV20_0.Account;
import bankingV20_0.Bank;


@RestController
public class HelloController {

	@RequestMapping("/")
	public String index() {
		return query();
	}
	
	@RequestMapping("/qrcode")
	public String qrcode() throws Exception {
		return getQRCode();
	}


	public String query() {
		String str = "error";
		
		try(Connection conn = DriverManager.getConnection("jdbc:postgresql://ec2-54-209-43-223.compute-1.amazonaws.com:5432/d19rc88931g1bi","luwrnpzmqrzvln","9b76f4cfa5a87feb4cf28e8b90e485b183bca39b2b20c09e323b4a04b524b2ce");
			Statement statement = conn.createStatement()) {
			
			String query = "select fname from students where studentno=17";
			
			ResultSet rs = statement.executeQuery(query);
			
			if(rs.next()) {
				return rs.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return str;
	}
	
	public String getQRCode() throws Exception
	{
		Bank aBank = new Bank();
		Account anAccount = aBank.getAccount(0);
		
		
		String imageBase64 = QRCodeGenerator.generateQRCodeImage(anAccount.getPubAddress(),250,250);
		//imageBase64 = QRCodeGenerator.generateQRCodeImage("991A38BED0D022D6622E9AD47513E2A14AC0DA58F15D8AFC81075DEC11CAF29D",250,250);
		return String.format("<div>Public Address: <a href=%s target=_blank>%s</a></div>", "https://creeper.banano.cc/explorer/account/"+ anAccount.getPubAddress(), anAccount.getPubAddress()) +
				String.format("<div>Account Number: %s</div>", anAccount.getAccountNo()) +
				String.format("<div>Balance: %s</div>", anAccount.getBalance()) +
				String.format("<img src=\"%s\"></img>", "data:image/png;base64," + imageBase64);

	}
	
}