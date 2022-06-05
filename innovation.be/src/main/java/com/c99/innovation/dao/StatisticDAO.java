package com.c99.innovation.dao;

import com.c99.innovation.common.enumtype.TimePeriodOption;
import com.c99.innovation.dto.response.ContributingCounting;
import com.c99.innovation.dto.response.TypesCountingInCertainTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StatisticDAO {
    List<TypesCountingInCertainTime> getTheCountingStatisticInTimePeriods(TimePeriodOption timePeriodOption);

    List<ContributingCounting> getContributingProjectsInACertainTime(LocalDateTime startDate, LocalDateTime endDate, Optional<Long> typeId, int limit);

    List<ContributingCounting> getTrendingAreasInACertainTime(LocalDateTime startDate, LocalDateTime endDate, Optional<Long> areaId, int limit);
}
