package com.c99.innovation.service.impl;

import com.c99.innovation.common.enumtype.TimePeriodOption;
import com.c99.innovation.dao.StatisticDAO;
import com.c99.innovation.dto.response.*;
import com.c99.innovation.dto.statistic.ComboStartDateEndDate;
import com.c99.innovation.entity.Area;
import com.c99.innovation.entity.CommentInnovation;
import com.c99.innovation.entity.Innovation;
import com.c99.innovation.repository.CommentRepository;
import com.c99.innovation.repository.InnovationAreaRepository;
import com.c99.innovation.repository.InnovationRepository;
import com.c99.innovation.security.UserDetailImpl;
import com.c99.innovation.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.c99.innovation.common.DatetimeUtils.getStartDateAndEndDateInAMonth;
import static com.c99.innovation.common.DatetimeUtils.getStartDateAndEndDateInAQuarter;
import static com.c99.innovation.common.DatetimeUtils.getStartDateAndEndDateInAYear;

@Service
public class StatisticServiceImpl implements StatisticService {

    private StatisticDAO statisticDAO;

    private InnovationRepository innovationRepository;

    private InnovationAreaRepository innovationAreaRepository;

    private CommentRepository commentRepository;

    @Autowired
    public StatisticServiceImpl(InnovationRepository statisticRepository,
                                StatisticDAO statisticDAO,
                                InnovationAreaRepository innovationAreaRepository,
                                CommentRepository commentRepository) {
        this.innovationRepository = statisticRepository;
        this.innovationAreaRepository = innovationAreaRepository;
        this.statisticDAO = statisticDAO;
        this.commentRepository = commentRepository;
    }

    /**
     * Get the summary of counting value of all types in the system
     *
     * @return TotalCountingInTypes
     */
    @Override
    public TotalCountingInTypes getInnovationIdeaImprovement() {

        long innovation = innovationRepository.getCountingByType(1);
        long improvement = innovationRepository.getCountingByType(2);
        long idea = innovationRepository.getCountingByType(3);
        long total = innovation + improvement + idea;
        return new TotalCountingInTypes(innovation, idea, improvement, total);
    }


    /**
     * Get the top innovation (at the present we just the latest innovations - need to change to the top trending
     * and interacting innovations)
     *
     * @param limit
     * @return List<TopInnovation>
     */
    @Override
    public List<TopInnovation> getTopInnovation(int limit) {
        Pageable pageRequest = PageRequest.of(0, limit);
        List<Innovation> topInnovation = innovationRepository.getLatestInnovationAllTypes(pageRequest);
        return topInnovation.stream()
                .map(innovation -> {
                    List<Area> areaList = this.innovationAreaRepository.findAllAreaByInnovationId(innovation.getId());
                    return new TopInnovation(innovation.getId(), innovation.getContent(), innovation.getTeam(), innovation.getType(), areaList, innovation.getCreatedAt());
                }).collect(Collectors.toList());
    }

    /**
     * Get the top contribuitng project which have many innovations has been raised by them
     *
     * @param limit
     * @return List<TopContributing>
     */
    @Override
    public List<TopContributing> getTopContributingProjects(int limit) {
        Pageable pageRequest = PageRequest.of(0, limit);
        return innovationRepository.getTopContributingProjectsAllTime(pageRequest);
    }

    @Override
    public List<TopInnovationByClap> getTopInnovationsByClap(int limit) {
        Pageable pageRequest = PageRequest.of(0, limit);
        return innovationRepository.getTopInnovationsByClap(pageRequest);
    }

    /**
     * Get the latest innovations of the current user
     *
     * @param limit
     * @return List<LatestOwnInnovation>
     */
    @Override
    public List<LatestOwnInnovation> getInnovationByCurrentAccount(int limit) {
        Pageable pageRequest = PageRequest.of(0, limit);
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return innovationRepository.getLatestInnovationByCurrentAccount(principal.getUsername(), pageRequest);
    }

    /**
     * Get the latest innovations of the current user
     *
     * @param
     * @return List<LatestOwnInnovation>
     */
    @Override
    public List<MyInnovationAreaResponse> getMyInnovation() {
        List<MyInnovationAreaResponse> myInnovationAreaResponses = new ArrayList<>();

        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<MyInnovation> myInnovations = innovationRepository.getMyInnovation(principal.getUsername());

        for (MyInnovation innovation: myInnovations) {
            List<String> areas = innovationRepository.getAreaByID(innovation.getId());
            MyInnovationAreaResponse myInnovationAreaResponse = new MyInnovationAreaResponse(innovation.getId(), innovation.getContent(),
                    innovation.getProjectName(), innovation.getTypeName(),
                    innovation.getCreatedAt(), innovation.getStatus(),areas);
            myInnovationAreaResponses.add(myInnovationAreaResponse);


        }

        return myInnovationAreaResponses;
    }

    @Override
    public List<TypesCountingInCertainTime> getTheCountingStatisticInTimePeriods(TimePeriodOption timePeriodOption) {
        return this.statisticDAO.getTheCountingStatisticInTimePeriods(timePeriodOption);
    }

    /**
     * Get the top contributing project in certain month or quarter or year
     * by processing the startDate and endDate to determine the right time period
     *
     * @param month
     * @param quarter
     * @param year
     * @param type
     * @param limit
     * @return List<ContributingCounting>
     */
    @Override
    public List<ContributingCounting> getTopContributingProjects(Optional<Integer> month, Optional<Integer> quarter, Integer year, Optional<Long> type, Integer limit) {
        ComboStartDateEndDate comboStartDateEndDate;
        if (!month.isEmpty()) {
            comboStartDateEndDate = getStartDateAndEndDateInAMonth(month.get(), year);
            return this.fetchTopContributingProjectsInACertainTime(comboStartDateEndDate, type, limit);
        }
        if (!quarter.isEmpty()) {
            comboStartDateEndDate = getStartDateAndEndDateInAQuarter(quarter.get(), year);
            return this.fetchTopContributingProjectsInACertainTime(comboStartDateEndDate, type, limit);
        }
        comboStartDateEndDate = getStartDateAndEndDateInAYear(year);
        return this.fetchTopContributingProjectsInACertainTime(comboStartDateEndDate, type, limit);
    }

    /**
     * Get the top trending areas in certain month or quarter or year
     * by processing the startDate and endDate to determine the right time period
     *
     * @param month
     * @param quarter
     * @param year
     * @param type
     * @param limit
     * @return List<ContributingCounting>
     */
    @Override
    public List<ContributingCounting> getTopTrendingAreas(Optional<Integer> month, Optional<Integer> quarter, Integer year, Optional<Long> type, Integer limit) {
        ComboStartDateEndDate comboStartDateEndDate;
        if (!month.isEmpty()) {
            comboStartDateEndDate = getStartDateAndEndDateInAMonth(month.get(), year);
            return this.fetchTopTrendingAreasInACertainTime(comboStartDateEndDate, type, limit);
        }
        if (!quarter.isEmpty()) {
            comboStartDateEndDate = getStartDateAndEndDateInAQuarter(quarter.get(), year);
            return this.fetchTopTrendingAreasInACertainTime(comboStartDateEndDate, type, limit);
        }
        comboStartDateEndDate = getStartDateAndEndDateInAYear(year);
        return this.fetchTopTrendingAreasInACertainTime(comboStartDateEndDate, type, limit);
    }

    @Override
    public List<LatestCommentsResponse> getLatestComments(int limit) {
        Pageable pageRequest = PageRequest.of(0, limit);
        List<CommentInnovation> commentList = commentRepository.getLatestComments(pageRequest);
        List<LatestCommentsResponse> commentsResponses = new ArrayList<>();
        for (CommentInnovation c : commentList) {
            commentsResponses.add(new LatestCommentsResponse(c.getContent(), c.getAccount().getUsername(), c.getInnovation().getId(), c.getInnovation().getContent(), c.getCreatedAt(), c.getLastModifiedAt()));
        }
        return commentsResponses;
    }

    /**
     * After having the startDate and endDate we call the DAO layer method to fetch the result for
     * top contributing projects in a certain time
     *
     * @param comboStartDateEndDate
     * @param typeId
     * @param limit
     * @return List<ContributingCounting>
     */
    private List<ContributingCounting> fetchTopContributingProjectsInACertainTime(ComboStartDateEndDate comboStartDateEndDate, Optional<Long> typeId, int limit) {
        return this.statisticDAO.getContributingProjectsInACertainTime(comboStartDateEndDate.getStartDate(), comboStartDateEndDate.getEndDate(), typeId, limit);
    }

    /**
     * After having the startDate and endDate we call the DAO layer method to fetch the result for
     * top trending areas in a certain time
     *
     * @param comboStartDateEndDate
     * @param typeId
     * @param limit
     * @return List<ContributingCounting>
     */
    private List<ContributingCounting> fetchTopTrendingAreasInACertainTime(ComboStartDateEndDate comboStartDateEndDate, Optional<Long> typeId, int limit) {
        return this.statisticDAO.getTrendingAreasInACertainTime(comboStartDateEndDate.getStartDate(), comboStartDateEndDate.getEndDate(), typeId, limit);
    }
}
