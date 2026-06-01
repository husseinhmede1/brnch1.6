package com.mdsl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdsl.exceptionHandling.BusinessException;
import com.mdsl.model.entity.PendingActivity;
import com.mdsl.model.entity.User;
import com.mdsl.repository.PendingActivityRepository;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.enumerations.ActivityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class WorkflowApprovalService {

    private final PendingActivityRepository pendingRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationContext applicationContext;
    private final StatusUpdateHelper statusUpdateHelper;

    public void approveActivity(Integer pendingActivityId) {

        PendingActivity activity = statusUpdateHelper.moveToProcessing(pendingActivityId);

        UserDetailsImpl currentChecker = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            Class<?> targetClass = Class.forName(activity.getClazz());
            Object serviceBean = applicationContext.getBean(targetClass);

            Method targetMethod = findTargetMethod(targetClass, activity.getMethod());
            Class<?> payloadType = targetMethod.getParameterTypes()[0];

            Object deserializedPayload = objectMapper.readValue(activity.getPayload(), payloadType);

            targetMethod.invoke(serviceBean, deserializedPayload);

            statusUpdateHelper.updateTerminalStatus(pendingActivityId, ActivityStatus.APPROVED, null, currentChecker.getId());

        } catch (Exception e) {
            String failureReason = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            statusUpdateHelper.updateTerminalStatus(pendingActivityId, ActivityStatus.PENDING, failureReason, currentChecker.getId());

            throw new RuntimeException("Execution failed. Activity marked as FAILED. Reason: " + failureReason, e);
        }
    }

    private Method findTargetMethod(Class<?> targetClass, String methodName) throws NoSuchMethodException {
        for (Method method : targetClass.getMethods()) {
            if (method.getName().equals(methodName) && method.getParameterCount() == 1) {
                return method;
            }
        }
        throw new NoSuchMethodException("Method " + methodName + " with a single parameter was not found on " + targetClass.getName());
    }

    @Transactional
    public void rejectActivity(Integer pendingActivityId, String rejectionNotes) {
        PendingActivity activity = pendingRepository.findById(pendingActivityId)
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_PENDING_ACTIVITY, HttpStatus.NOT_FOUND));

        if (!ActivityStatus.PENDING.equals(activity.getStatus())) {
            throw new BusinessException(ResponseCode.CFG_INVALID_PENDING_ACTIVITY, HttpStatus.BAD_REQUEST);
        }

        UserDetailsImpl currentChecker = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        activity.setStatus(ActivityStatus.DECLINED);
        activity.setNotes(rejectionNotes);
        activity.setUpdatedDate(now);
        activity.setUpdatedBy(currentChecker.getId());

        pendingRepository.save(activity);
    }
}

@Component
@RequiredArgsConstructor
class StatusUpdateHelper {

    private final PendingActivityRepository pendingRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public PendingActivity moveToProcessing(Integer pendingActivityId) {
        // Fetch record (preferably using a row lock depending on your repository design)
        PendingActivity activity = pendingRepository.findById(pendingActivityId)
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_PENDING_ACTIVITY, HttpStatus.NOT_FOUND));

        if (!ActivityStatus.PENDING.equals(activity.getStatus())) {
            throw new BusinessException(ResponseCode.CFG_INVALID_PENDING_ACTIVITY, HttpStatus.BAD_REQUEST);
        }

        UserDetailsImpl currentChecker = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Rule Validation: Maker cannot Checker check
        if (activity.getCreatedBy().getUserId().equals(currentChecker.getId())) {
            throw new BusinessException(ResponseCode.VAL_INVALID_ACCESS, HttpStatus.FORBIDDEN);
        }

        // Set state to processing
        Timestamp now = new Timestamp(System.currentTimeMillis());
        activity.setStatus(ActivityStatus.PROCESSING);
        activity.setUpdatedDate(now);
        activity.setUpdatedBy(currentChecker.getId());
        return pendingRepository.saveAndFlush(activity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateTerminalStatus(Integer pendingActivityId, ActivityStatus finalStatus, String notes, Integer updatedByUserId) {
        PendingActivity activity = pendingRepository.findById(pendingActivityId)
                .orElseThrow(() -> new BusinessException(ResponseCode.CFG_INVALID_PENDING_ACTIVITY, HttpStatus.NOT_FOUND));

        Timestamp now = new Timestamp(System.currentTimeMillis());
        activity.setStatus(finalStatus);
        activity.setUpdatedDate(now);
        activity.setUpdatedBy(updatedByUserId);
        activity.setNotes(notes);
        pendingRepository.saveAndFlush(activity);
    }
}