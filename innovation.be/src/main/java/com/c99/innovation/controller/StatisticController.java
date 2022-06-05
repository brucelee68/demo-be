package com.c99.innovation.controller;

import com.c99.innovation.common.enumtype.TimePeriodOption;
import com.c99.innovation.common.validation.OptionalIntegerValue;
import com.c99.innovation.dto.ResponseDTO;
import com.c99.innovation.dto.response.ContributingCounting;
import com.c99.innovation.dto.response.PersonalInnovationResponse;
import com.c99.innovation.dto.response.SummaryStatistic;
import com.c99.innovation.dto.response.TypesCountingInCertainTime;
import com.c99.innovation.service.StatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.c99.innovation.common.constant.Constants.API_V1;
import static com.c99.innovation.common.constant.Constants.BAD_REQUEST_COMMON_MESSAGE;
import static com.c99.innovation.common.constant.Constants.FETCHING_SUCCESSFULLY;

@Api(value = "Statistic Controller")
@RestController
@RequestMapping(value = API_V1 + "/statistic")
@Validated
public class StatisticController {

    private StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/summary")
    public ResponseDTO<SummaryStatistic> homepageInnovationAccount(
            @RequestParam(name = "_trend") @OptionalIntegerValue(min = 1) Optional<Integer> trendingInnovationLimit,
            @RequestParam(name = "_project") @OptionalIntegerValue(min = 1) Optional<Integer> contributingProjectLimit,
            @RequestParam(name = "_comment") @OptionalIntegerValue(min = 1) Optional<Integer> latestCommentsLimit,
            @RequestParam(name = "_own") @OptionalIntegerValue(min = 1) Optional<Integer> currentUserInnovationLimit,
            @RequestParam(name = "_clap") @OptionalIntegerValue(min = 1) Optional<Integer> topInnovationsByClapLimit) {
        SummaryStatistic summaryStatistic = new SummaryStatistic(
                this.statisticService.getInnovationIdeaImprovement(),
                this.statisticService.getTopInnovation(trendingInnovationLimit.orElse(5)),
                this.statisticService.getTopContributingProjects(contributingProjectLimit.orElse(4)),
                this.statisticService.getInnovationByCurrentAccount(currentUserInnovationLimit.orElse(4)),
                this.statisticService.getTopInnovationsByClap(topInnovationsByClapLimit.orElse(4)),
                this.statisticService.getLatestComments(latestCommentsLimit.orElse(4)));
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, summaryStatistic);
    }

    @GetMapping("/my/innovation")
    public ResponseDTO<PersonalInnovationResponse> myInnovationAccount() {
        PersonalInnovationResponse myInnovationResponse = new PersonalInnovationResponse(
                this.statisticService.getMyInnovation());
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, myInnovationResponse);
    }

    @ApiOperation(value = "Get counting statistic im each type innovation/improvement/idea based on recent months")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @GetMapping("/in/months")
    public ResponseDTO<List<TypesCountingInCertainTime>> getStatisticInMonths() {
        List<TypesCountingInCertainTime> listStatisticInMonths = this.statisticService.getTheCountingStatisticInTimePeriods(TimePeriodOption.MONTH);
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, listStatisticInMonths);
    }

    @ApiOperation(value = "Get counting statistic im each type innovation/improvement/idea based on recent quarters")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @GetMapping("/in/quarters")
    public ResponseDTO<List<TypesCountingInCertainTime>> getStatisticInQuarters() {
        List<TypesCountingInCertainTime> listStatisticInQuarters = this.statisticService.getTheCountingStatisticInTimePeriods(TimePeriodOption.QUARTER);
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, listStatisticInQuarters);
    }

    @ApiOperation(value = "Get counting statistic im each type innovation/improvement/idea based on recent years")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @GetMapping("/in/years")
    public ResponseDTO<List<TypesCountingInCertainTime>> getStatisticInYears() {
        List<TypesCountingInCertainTime> listStatisticInYears = this.statisticService.getTheCountingStatisticInTimePeriods(TimePeriodOption.YEAR);
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, listStatisticInYears);
    }

    @ApiOperation(value = "Get top contributing projects im one particular type innovation/improvement/idea in a certain time month/quarter/year")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @GetMapping("/top/projects")
    public ResponseDTO<List<ContributingCounting>> getTopContributingProjects(
            @RequestParam(value = "months") @OptionalIntegerValue(min = 1, max = 12) Optional<Integer> month,
            @RequestParam(value = "quarters") @OptionalIntegerValue(min = 1, max = 4) Optional<Integer> quarter,
            @RequestParam(value = "years") Integer year,
            @RequestParam(value = "type") Optional<Long> type,
            @RequestParam(value = "limit") @OptionalIntegerValue(min = 1) Optional<Integer> limit) {
        List<ContributingCounting> topContributingProjects = this.statisticService.getTopContributingProjects(month, quarter, year, type, limit.orElse(5));
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, topContributingProjects);
    }

    @ApiOperation(value = "Get top contributing projects im one particular type innovation/improvement/idea in a certain time month/quarter/year")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY),
            @ApiResponse(code = 400, message = BAD_REQUEST_COMMON_MESSAGE)})
    @GetMapping("/top/areas")
    public ResponseDTO<List<ContributingCounting>> getTopContributingAreas(
            @RequestParam(value = "months") @OptionalIntegerValue(min = 1, max = 12) Optional<Integer> month,
            @RequestParam(value = "quarters") @OptionalIntegerValue(min = 1, max = 4) Optional<Integer> quarter,
            @RequestParam(value = "years") Integer year,
            @RequestParam(value = "type") Optional<Long> type,
            @RequestParam(value = "limit") @OptionalIntegerValue(min = 1) Optional<Integer> limit) {
        List<ContributingCounting> topTrendingAreas = this.statisticService.getTopTrendingAreas(month, quarter, year, type, limit.orElse(5));
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, topTrendingAreas);
    }
}
