package es.unizar.smartcampuz.infrastructure.service;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
	
	@Autowired
	private SmtpMailService smtpMailSender;

	@RequestMapping("/send-mail")
	public void sendMail() throws MessagingException {
		
		smtpMailSender.sendReservationEmail("catalindumitrache76@gmail.com", 1, true);
		
	}
	
}