package com.mdsl.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mdsl.service.EntityTerminationNotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EntityTerminationNotificationScheduler {

    private final EntityTerminationNotificationService notificationService;

    /**
     * Runs daily at 8:00 AM
     * Cron format: second, minute, hour, day of month, month, day of week
     */
    @Scheduled(cron = "0 0 8 * * ?")
//    @Scheduled(cron = "*/20 * * * * ?")
    public void scheduleEntityTerminationNotifications() {
        log.info("EntityTerminationNotificationScheduler triggered");
        notificationService.processTerminationNotifications();
    }
}