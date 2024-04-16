package com.innovx.gestionrh.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

@Service
public class EmailServiceImpl implements EmailService {

    // Injecting values from properties file or configuration
    @Value("houarimehdi7@gmail.com")
    private String userName;

    @Value("EE59B07197F2B01E1C201044C6BB489BA273")
    private String apiKey;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            String from = "houarimehdi7@gmail.com"; // Change this to your email address
            String fromName = "innovx"; // Change this to your name
            String body = text;
            String isTransactional = "false"; // Assuming this is not a transactional email

            String encoding = "UTF-8";

            String data = "apikey=" + URLEncoder.encode("EE59B07197F2B01E1C201044C6BB489BA273", encoding);
            data += "&from=" + URLEncoder.encode(from, encoding);
            data += "&fromName=" + URLEncoder.encode(fromName, encoding);
            data += "&subject=" + URLEncoder.encode(subject, encoding);
            data += "&bodyHtml=" + URLEncoder.encode(body, encoding);
            data += "&to=" + URLEncoder.encode(to, encoding);
            data += "&isTransactional=" + URLEncoder.encode(isTransactional, encoding);

            URL url = new URL("https://api.elasticemail.com/v2/email/send");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = rd.readLine();
            wr.close();
            rd.close();

            System.out.println(result); // Assuming you want to print the result
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
