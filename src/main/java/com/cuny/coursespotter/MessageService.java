package com.cuny.coursespotter;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class MessageService {

    private SendGrid sendGrid;

    public String accountSid;
    public String authToken;
    public String sendGridKey;

    public MessageService() {
        loadConfiguration();
        Twilio.init(accountSid, authToken);
        sendGrid = new SendGrid(sendGridKey);
    }

    /**
     * Fetch necessary info from the configuration file
     */
    private void loadConfiguration() {

        try (InputStream config = getClass().getClassLoader().getResourceAsStream("config.properties")){
            Properties properties = new Properties();

            if(config == null) {
                System.out.println("Unable to read config file.");
                return;
            }

            properties.load(config);

            accountSid = properties.getProperty("twilio.accountsid");
            authToken = properties.getProperty("twilio.authtoken");
            sendGridKey = properties.getProperty("sendgrid.key");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an email message to the recipient using the following subject and body.
     *
     * @param from    email address to send from
     * @param subject email subject line
     * @param to      email address to send to
     * @param content email body
     * @return An integer value which is the response status code
     */
    public int sendEmail(String from, String subject, String to, Content content) {
        Mail mail = new Mail(new Email(from), subject, new Email(to), content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            return response.getStatusCode();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * Sends a text message to the recipient with the specified message.
     *
     * @param to   To number
     * @param from From number
     * @param body Message body
     * @return String containing message SID
     */
    public String sendText(String to, String from, String body) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(from),
                    body
            ).create();

            return message.getSid();
        } catch (final ApiException e) {
            e.printStackTrace();
        }
        return "";
    }
}
