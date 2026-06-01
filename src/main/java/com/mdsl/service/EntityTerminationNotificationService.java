package com.mdsl.service;


import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mdsl.model.entity.Employee;
import com.mdsl.model.entity.Entities;
import com.mdsl.model.entity.SystemCode;
import com.mdsl.repository.EntitiesRepository;
import com.mdsl.repository.SystemCodeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntityTerminationNotificationService {
	
	@Value("${spring.mail.username}")
	private String fromEmail;
    private static final String NOTIFICATION_PREFIX = "NOTIFICATION_SCHEDULER";
    private static final String FREQUENCY_SUFFIX = "NOTIF_SCH_FREQUENCY";
    private static final String MONTHS_SUFFIX = "NOTIF_SCH_MONTH";
    
    private static final String FREQUENCY_DAILY = "DAILY";
    private static final String FREQUENCY_WEEKLY = "WEEKLY";
    private static final String FREQUENCY_MONTHLY = "MONTHLY";
    private final EntitiesRepository entitiesRepository;
    private final EmailTemplateService emailTemplateService;
    private final SystemCodeRepository systemCodeRepository;
    private final EmailSenderService emailService;

    @Transactional(readOnly = true)
    public void processTerminationNotifications() {
        log.info("Starting entity termination notification job");

        try {
            // Fetch configuration parameters
            Integer notificationMonths = getNotificationMonths();
            String notificationFrequency = getNotificationFrequency();

            if (notificationMonths == null || notificationFrequency == null) {
                log.warn("Notification configuration not found. Skipping job execution.");
                return;
            }

            log.info("Configuration - Months: {}, Frequency: {}", notificationMonths, notificationFrequency);

            // Fetch entities with upcoming termination
            Date currentDate = new Date();
            List<Entities> entities = entitiesRepository.findAllWithFutureTermination();
            
            log.info("Found {} entities with termination dates", entities.size());

            int notificationsSent = 0;
            for (Entities entity : entities) {
                if (shouldSendNotification(entity, notificationMonths, notificationFrequency, currentDate)) {
                    sendNotification(entity, notificationMonths);
                    notificationsSent++;
                }
            }

            log.info("Entity termination notification job completed. Sent {} notifications", notificationsSent);

        } catch (Exception e) {
            log.error("Error processing entity termination notifications", e);
        }
    }

    private Integer getNotificationMonths() {
        return systemCodeRepository
            .findByCodePrefixAndCodeSuffixAndStatus(NOTIFICATION_PREFIX, MONTHS_SUFFIX,'1')
            .map(SystemCode::getCodeValue)
            .map(value -> {
                try {
                    return Integer.parseInt(value.trim());
                } catch (NumberFormatException e) {
                    log.error("Invalid months configuration value: {}", value);
                    return null;
                }
            })
            .orElse(null);
    }

    private String getNotificationFrequency() {
        return systemCodeRepository
            .findByCodePrefixAndCodeSuffixAndStatus(NOTIFICATION_PREFIX, FREQUENCY_SUFFIX,'1')
            .map(SystemCode::getCodeValue)
            .map(String::trim)
            .map(String::toUpperCase)
            .orElse(null);
    }

    private boolean shouldSendNotification(Entities entity, Integer notificationMonths, 
                                          String frequency, Date currentDate) {
        
        LocalDate today = LocalDate.now();
        LocalDate terminationDate = entity.getTerminationDate()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

        // Calculate months between current date and termination date
        long monthsBetween = ChronoUnit.MONTHS.between(today, terminationDate);
        
        // Check if we're within the notification window
        if (monthsBetween > notificationMonths || monthsBetween < 0) {
            return false;
        }

        // Calculate days remaining
        long daysRemaining = ChronoUnit.DAYS.between(today, terminationDate);

        switch (frequency) {
            case FREQUENCY_DAILY:
                return true;

            case FREQUENCY_WEEKLY:
                return daysRemaining % 7 == 0;

            case FREQUENCY_MONTHLY:
                return today.getDayOfMonth() == 1;

            default:
                log.warn("Unknown frequency type: {}. Defaulting to daily.", frequency);
                return true;
        }
    }

//    private void sendNotification(Entities entity, Integer notificationMonths) {
//        try {
//            Employee employeeInCharge = entity.getEmployeeIncharge();
//            
//            if (employeeInCharge == null) {
//                log.warn("No employee in charge for entity: {}", entity.getEntityId());
//                return;
//            }
//
//            LocalDate terminationDate = entity.getTerminationDate()
//                .toInstant()
//                .atZone(ZoneId.systemDefault())
//                .toLocalDate();
//            
//            long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), terminationDate);
//            long monthsRemaining = ChronoUnit.MONTHS.between(LocalDate.now(), terminationDate);
//
//            String subject = String.format("Entity Contract Termination Reminder - %s", entity.getEntityName());
//            
//            String body = buildEmailBody(entity, terminationDate, daysRemaining, monthsRemaining);
//
//            // Assuming EmailService has a method to send emails
//            // You'll need to adjust this based on your actual email service implementation
//            emailService.sendMail(
//            	fromEmail,
//                employeeInCharge.getEmployeeEmail(), // Assuming Employee has email field
//                subject,
//                body
//            );
//
//            log.info("Notification sent for entity: {} to employee: {}", 
//                     entity.getEntityId(), employeeInCharge.getEmployeeId());
//
//        } catch (Exception e) {
//            log.error("Failed to send notification for entity: {}", entity.getEntityId(), e);
//        }
//    }
//
//    private String buildEmailBody(Entities entity, LocalDate terminationDate, 
//                                  long daysRemaining, long monthsRemaining) {
//        return String.format(
//            "Dear Employee,%n%n" +
//            "This is a reminder that the contract for the following entity is approaching termination:%n%n" +
//            "Entity ID: %s%n" +
//            "Entity Name: %s%n" +
//            "Termination Date: %s%n" +
//            "Days Remaining: %d days%n" +
//            "Months Remaining: %d months%n%n" +
//            "Please take the necessary actions to renew or terminate this contract.%n%n" +
//            "Best regards,%n" +
//            "System Notification",
//            entity.getEntityId(),
//            entity.getEntityName(),
//            terminationDate,
//            daysRemaining,
//            monthsRemaining
//        );
//    }
    private void sendNotification(Entities entity, Integer notificationMonths) {
        try {
            Employee employeeInCharge = entity.getEmployeeIncharge();
            
            if (employeeInCharge == null) {
                log.warn("No employee in charge for entity: {}", entity.getEntityId());
                return;
            }
            
            LocalDate terminationDate = entity.getTerminationDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
            
            long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), terminationDate);
            long monthsRemaining = ChronoUnit.MONTHS.between(LocalDate.now(), terminationDate);
            
            String subject = String.format("Entity Contract Termination Reminder - %s", entity.getEntityName());
            
            // Build HTML email from template
            String htmlBody = buildHtmlEmailBody(entity, employeeInCharge, terminationDate, daysRemaining, monthsRemaining);
            
            emailService.sendHtmlMail(
                fromEmail,
                employeeInCharge.getEmployeeEmail(),
                subject,
                htmlBody
            );
            
            log.info("Notification sent for entity: {} to employee: {}", 
                     entity.getEntityId(), employeeInCharge.getEmployeeId());
        } catch (Exception e) {
            log.error("Failed to send notification for entity: {}", entity.getEntityId(), e);
        }
    }

    private String buildHtmlEmailBody(Entities entity, Employee employee, LocalDate terminationDate, 
                                      long daysRemaining, long monthsRemaining) {
        try {
            Map<String, String> parameters = Map.of(
                "EMPLOYEE_NAME", employee.getEmployeeName() != null ? employee.getEmployeeName() : "Employee",
                "ENTITY_ID", String.valueOf(entity.getEntityId()),
                "ENTITY_NAME", entity.getEntityName() != null ? entity.getEntityName() : "N/A",
                "TERMINATION_DATE", terminationDate.toString(),
                "DAYS_REMAINING", String.valueOf(daysRemaining),
                "MONTHS_REMAINING", String.valueOf(monthsRemaining),
                "URGENCY_CLASS", daysRemaining < 30 ? "urgent" : ""
            );
            
            return emailTemplateService.loadTemplate("masTerminationMail.html", parameters);
        } catch (IOException e) {
            log.error("Failed to load email template, falling back to plain text", e);
            // Fallback to plain text if template fails
            return buildFallbackPlainText(entity, terminationDate, daysRemaining, monthsRemaining);
        }
    }

    private String buildFallbackPlainText(Entities entity, LocalDate terminationDate, 
                                          long daysRemaining, long monthsRemaining) {
        return String.format(
            "Dear Employee,%n%n" +
            "Entity ID: %s%n" +
            "Entity Name: %s%n" +
            "Termination Date: %s%n" +
            "Days Remaining: %d days%n%n" +
            "Best regards,%n" +
            "System Notification",
            entity.getEntityId(),
            entity.getEntityName(),
            terminationDate,
            daysRemaining
        );
    }
}