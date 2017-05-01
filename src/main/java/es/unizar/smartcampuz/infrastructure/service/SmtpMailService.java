package es.unizar.smartcampuz.infrastructure.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * He creado los ficheros RootController.java, SmtpMailService.java
 * He modificado el AdminDashboardController para enviar el email cuando aprueban o no la reserva (en /reservation)
 * He modificado el application.properties con las credenciales de gmail y la configuración de smtp.
 * @author catalin
 */



@Component
public class SmtpMailService {

	@Autowired	
    private JavaMailSender javaMailSender;
    
    public void sendReservationEmail(String to, long reservationId, boolean approved) {

    	MimeMessage message = javaMailSender.createMimeMessage();
    	
    	try {

    		// true indicates multipart message
    		MimeMessageHelper helper = new MimeMessageHelper(message, true);

    		helper.setSubject("SmartCampUZ");
    		helper.setTo(to);

    		if (approved) {
    			// If the reservation was approved
    			helper.setText("Hola. Tu reserva <"+ reservationId +"> en SmartCampuz ha sido realizada con éxito.", true);
    		} else {
    			// If the reservation was denied
    			helper.setText("Hola. Tu reserva <"+ reservationId +"> en SmartCampuz ha sido realizada con éxito.", true);
    		}
    		
    		javaMailSender.send(message);

    		
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // true indicates
		
    	
    }
    
    @Override
    public String toString(){
    	return "From: " + //templateMessage.getFrom() + 
    			"; To: " + //templateMessage.getTo() + 
    			"; Subject: "+ //templateMessage.getSubject() + 
    			"; Text: ";// + templateMessage.getText();
    }

}