/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brugerautorisation.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author j
 */
public class SendMail {

  public static void sendMail(String emne, String tekst, String modtagere) throws MessagingException {
    // Husk først at sænke sikkerheden på https://www.google.com/settings/security/lesssecureapps
    final String afsender = "android.ihk@gmail.com";
    System.out.println("sendMail " + emne + " " + modtagere + " " + tekst.replace('\n', ' '));
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    Session session = Session.getInstance(props, new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        Path kodefil = Paths.get("gmail-adgangskode.txt");
        try {
          String adgangskode = new String(Files.readAllBytes(kodefil));
          return new PasswordAuthentication(afsender, adgangskode);
        } catch (IOException ex) {
          System.err.println("Du kan ikke sende mails f\u00f8r du har konfigurerer afsender (" + afsender + ") til lav sikkerhed:\nhttps://www.google.com/settings/security/lesssecureapps\nog og lagt adgangskoden i " + kodefil);
          ex.printStackTrace();
        }
        return null;
      }
    });
    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(afsender));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(modtagere));
    message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("jacob.nordfalk@gmail.com"));
    message.setSubject(emne);
    message.setText(tekst);
    Transport.send(message);
  }
  
}
