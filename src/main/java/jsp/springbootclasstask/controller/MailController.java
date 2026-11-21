package jsp.springbootclasstask.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jsp.springbootclasstask.service.EmailService;

@RestController
public class MailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/sendMail")
    public String sendMail() {
        try {
			emailService.sendHtmlEmail(
			    "amitash600@gmail.com",
			    "Test Mail",
			    "Hello from Spring Boot Java Mail Sender!"
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "Mail sent successfully";
    }
}
