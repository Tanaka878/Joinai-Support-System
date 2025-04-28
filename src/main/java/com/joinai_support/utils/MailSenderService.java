package com.joinai_support.utils;

import com.joinai_support.domain.Admin;
import com.joinai_support.domain.SupportTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MailSenderService {
    private static final Logger logger = LoggerFactory.getLogger(MailSenderService.class);

    private final JavaMailSender mailSender;

    @Autowired
    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a welcome email to a new admin/agent
     * @param to Email address of the recipient
     * @param name Name of the recipient
     * @param password Initial password for the account
     */
    public void sendWelcomeEmail(String to, String name, String password) {
        String subject = "Welcome to JoinAI Support Platform";
        String text = "Hello " + name + ",\n\n" +
                "Welcome to the JoinAI Support Platform. Your account has been created successfully.\n\n" +
                "Your login credentials:\n" +
                "Email: " + to + "\n" +
                "Password: " + password + "\n\n" +
                "Please change your password after the first login.\n\n" +
                "Best Regards,\nThe JoinAI Support Team";

        sendEmail(to, subject, text);
    }

    /**
     * Sends a notification when a new ticket is created
     * @param ticket The support ticket that was created
     * @param admin The admin/agent the ticket is assigned to
     */
    public void sendTicketCreationNotification(SupportTicket ticket, Admin admin) {
        String subject = "New Support Ticket Assigned - #" + ticket.getId();
        String text = "Hello " + admin.getFirstName() + ",\n\n" +
                "A new support ticket has been assigned to you:\n\n" +
                "Ticket ID: " + ticket.getId() + "\n" +
                "Subject: " + ticket.getSubject() + "\n" +
                "Priority: " + ticket.getPriority() + "\n" +
                "Category: " + ticket.getCategory() + "\n" +
                "Created: " + ticket.getLaunchTimestamp() + "\n\n" +
                "Please log in to the support platform to view the details and respond to this ticket.\n\n" +
                "Best Regards,\nThe JoinAI Support Team";

        sendEmail(admin.getEmail(), subject, text);
    }

    /**
     * Sends a notification when a ticket status is updated
     * @param ticket The support ticket that was updated
     * @param admin The admin/agent who is assigned to the ticket
     */
    public void sendTicketUpdateNotification(SupportTicket ticket, Admin admin) {
        String subject = "Support Ticket Updated - #" + ticket.getId();
        String text = "Hello " + admin.getFirstName() + ",\n\n" +
                "A support ticket assigned to you has been updated:\n\n" +
                "Ticket ID: " + ticket.getId() + "\n" +
                "Subject: " + ticket.getSubject() + "\n" +
                "Status: " + ticket.getStatus() + "\n" +
                "Priority: " + ticket.getPriority() + "\n" +
                "Last Updated: " + ticket.getServedTimestamp() + "\n\n" +
                "Please log in to the support platform to view the details.\n\n" +
                "Best Regards,\nThe JoinAI Support Team";

        sendEmail(admin.getEmail(), subject, text);
    }

    /**
     * Sends a notification to the ticket issuer when their ticket is closed
     * @param ticket The support ticket that was closed
     */
    public void sendTicketClosedNotification(SupportTicket ticket) {
        if (ticket.getIssuerEmail() == null || ticket.getIssuerEmail().isEmpty()) {
            logger.warn("Cannot send ticket closed notification: issuer email is missing for ticket ID: {}", ticket.getId());
            return;
        }

        String subject = "Your Support Ticket Has Been Closed - #" + ticket.getId();
        String text = "Hello,\n\n" +
                "Your support ticket has been closed:\n\n" +
                "Ticket ID: " + ticket.getId() + "\n" +
                "Subject: " + ticket.getSubject() + "\n" +
                "Status: " + ticket.getStatus() + "\n" +
                "Priority: " + ticket.getPriority() + "\n" +
                "Closed At: " + ticket.getServedTimestamp() + "\n\n" +
                "If you have any further questions or if you believe this ticket was closed in error, " +
                "please feel free to create a new support ticket or reply to this email.\n\n" +
                "Thank you for using our support services.\n\n" +
                "Best Regards,\nThe JoinAI Support Team";

        sendEmail(ticket.getIssuerEmail(), subject, text);
    }

    /**
     * Sends a password reset email with OTP
     * @param otp One-time password for resetting the password
     * @param email Email address of the recipient
     */
    public void sendPasswordResetEmail(String otp, String email) {
        String subject = "Password Reset Request - JoinAI Support Platform";
        String text = "Hello,\n\n" +
                "We received a request to reset your password for the JoinAI Support Platform.\n\n" +
                "Your one-time password (OTP) to reset your password is: " + otp + "\n\n" +
                "If you did not request a password reset, please ignore this email or contact support.\n\n" +
                "Best Regards,\nThe JoinAI Support Team";

        sendEmail(email, subject, text);
    }

    /**
     * Helper method to send emails
     * @param to Email address of the recipient
     * @param subject Subject of the email
     * @param text Body of the email
     */
    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            logger.info("Email sent successfully to {}", to);
        } catch (MailException e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
