package com.example.btp400a2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping("/")
	public String index() {
		return query();
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
}
