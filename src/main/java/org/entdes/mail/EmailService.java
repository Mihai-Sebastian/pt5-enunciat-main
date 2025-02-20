package org.entdes.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Implementació real d'EmailService
@Service
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;
    private final String remitent;

    @Autowired
    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.from}") String remitent) {
        this.mailSender = mailSender;
        this.remitent = remitent;
    }

    public void enviarCorreu(String destinatari, String assumpte, String cos) throws Exception {

        // Enviar correu
        if(destinatari == null || destinatari.trim().isEmpty()) {
            throw new Exception("No s'ha especificat cap destinatari");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatari);
        message.setFrom(this.remitent); // Configura el paràmetre from
        message.setSubject(assumpte);
        message.setText(cos);
        mailSender.send(message);
    }

}
