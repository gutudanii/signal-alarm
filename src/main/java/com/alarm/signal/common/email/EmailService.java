package com.alarm.signal.common.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.from:Signal Alarm <gutudanielgeleta@gmail.com>}")
    private String mailFrom;

    public void sendHtmlMessage(String to, String subject,
                                String templateName, Map<String, Object> variables,
                                List<String> cc, List<String> bcc, List<Attachment> attachments) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(mailFrom);
        if (cc != null && !cc.isEmpty()) {
            helper.setCc(cc.toArray(new String[0]));
        }
        if (bcc != null && !bcc.isEmpty()) {
            helper.setBcc(bcc.toArray(new String[0]));
        }
        Context context = new Context();
        if (variables != null) {
            context.setVariables(variables);
        }
        String html = templateEngine.process(templateName, context);
        helper.setText(html, true);
        if (attachments != null) {
            for (Attachment att : attachments) {
                helper.addAttachment(att.getFilename(), att.getFile());
            }
        }
        mailSender.send(message);
    }

    public static class Attachment {
        private final String filename;
        private final File file;
        public Attachment(String filename, File file) {
            this.filename = filename;
            this.file = file;
        }
        public String getFilename() {
            return filename;
        }
        public java.io.File getFile() {
            return file;
        }
    }
}

