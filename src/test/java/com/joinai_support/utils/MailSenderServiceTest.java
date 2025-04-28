package com.joinai_support.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailSenderServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailSenderService mailSenderService;

    @Test
    public void testIsValidEmail() throws Exception {
        // Use ReflectionTestUtils to test the private method
        // Test valid email addresses
        assertTrue((Boolean) ReflectionTestUtils.invokeMethod(mailSenderService, "isValidEmail", "test@example.com"));
        assertTrue((Boolean) ReflectionTestUtils.invokeMethod(mailSenderService, "isValidEmail", "user.name@domain.com"));
        assertTrue((Boolean) ReflectionTestUtils.invokeMethod(mailSenderService, "isValidEmail", "user+tag@example.com"));
        assertTrue((Boolean) ReflectionTestUtils.invokeMethod(mailSenderService, "isValidEmail", "user-name@domain.co.uk"));

        // Test invalid email addresses
        assertFalse((Boolean) ReflectionTestUtils.invokeMethod(mailSenderService, "isValidEmail", (Object) null));
        assertFalse((Boolean) ReflectionTestUtils.invokeMethod(mailSenderService, "isValidEmail", ""));
        assertFalse((Boolean) ReflectionTestUtils.invokeMethod(mailSenderService, "isValidEmail", "Sample"));
        assertFalse((Boolean) ReflectionTestUtils.invokeMethod(mailSenderService, "isValidEmail", "user@"));
        assertFalse((Boolean) ReflectionTestUtils.invokeMethod(mailSenderService, "isValidEmail", "@domain.com"));
        assertFalse((Boolean) ReflectionTestUtils.invokeMethod(mailSenderService, "isValidEmail", "user@domain"));
        assertFalse((Boolean) ReflectionTestUtils.invokeMethod(mailSenderService, "isValidEmail", "user name@domain.com"));
    }

    @Test
    public void testSendEmailWithInvalidAddress() throws Exception {
        // Get the private method using reflection
        Method sendEmailMethod = MailSenderService.class.getDeclaredMethod("sendEmail", String.class, String.class, String.class);
        sendEmailMethod.setAccessible(true);

        // Test with invalid email address
        sendEmailMethod.invoke(mailSenderService, "Sample", "Test Subject", "Test Body");

        // Verify that mailSender.send() was never called
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendEmailWithValidAddress() throws Exception {
        // Get the private method using reflection
        Method sendEmailMethod = MailSenderService.class.getDeclaredMethod("sendEmail", String.class, String.class, String.class);
        sendEmailMethod.setAccessible(true);

        // Test with valid email address
        sendEmailMethod.invoke(mailSenderService, "test@example.com", "Test Subject", "Test Body");

        // Verify that mailSender.send() was called once
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
