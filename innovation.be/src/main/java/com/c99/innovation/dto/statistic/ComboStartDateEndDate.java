package com.c99.innovation.dto.statistic;

import java.time.LocalDateTime;

public class ComboStartDateEndDate {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public ComboStartDateEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
