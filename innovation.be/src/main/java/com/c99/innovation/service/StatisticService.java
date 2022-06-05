package com.c99.innovation.service;

import com.c99.innovation.common.enumtype.TimePeriodOption;
import com.c99.innovation.dto.response.*;

import java.util.List;
import java.util.Optional;

public interface StatisticService {

    TotalCountingInTypes getInnovationIdeaImprovement();

    List<TopInnovation> getTopInnovation(int count);

    List<TopContributing> getTopContributingProjects(int count);

    List<TopInnovationByClap> getTopInnovationsByClap(int count);

    List<LatestOwnInnovation> getInnovationByCurrentAccount(int count);

    List<MyInnovationAreaResponse> getMyInnovation();

    List<TypesCountingInCertainTime> getTheCountingStatisticInTimePeriods(TimePeriodOption timePeriodOption);

    List<ContributingCounting> getTopContributingProjects(Optional<Integer> month, Optional<Integer> quarter, Integer year, Optional<Long> type, Integer limit);

    List<ContributingCounting> getTopTrendingAreas(Optional<Integer> month, Optional<Integer> quarter, Integer year, Optional<Long> type, Integer limit);

    List<LatestCommentsResponse> getLatestComments(int count);
}
