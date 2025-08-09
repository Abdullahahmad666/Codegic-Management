package com.codegic.management.service;

import com.codegic.management.entity.SalaryAdjustHistory;
import com.codegic.management.repository.SalaryAdjustRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SalaryAdjustHistoryService {

    private final SalaryAdjustRepository historyRepository;

    public SalaryAdjustHistoryService(SalaryAdjustRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public boolean wasAdjustedRecently(Long departmentId, int minutes) {
        Optional<SalaryAdjustHistory> lastAdjustment =
                historyRepository.findTopByDepartmentIdOrderByAdjustmentTimeDesc(departmentId);

        return lastAdjustment.isPresent() &&
               lastAdjustment.get().getAdjustmentTime().plusMinutes(minutes).isAfter(LocalDateTime.now());
    }

    public void recordAdjustment(Long departmentId) {
        SalaryAdjustHistory history = new SalaryAdjustHistory();
        history.setDepartmentId(departmentId);
        history.setAdjustmentTime(LocalDateTime.now());
        historyRepository.save(history);
    }
}
