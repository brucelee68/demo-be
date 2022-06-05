package com.c99.innovation.dao.impl;

import com.c99.innovation.common.enumtype.InnovationType;
import com.c99.innovation.common.enumtype.TimePeriodOption;
import com.c99.innovation.dao.StatisticDAO;
import com.c99.innovation.dto.response.ContributingCounting;
import com.c99.innovation.dto.response.TypesCountingInCertainTime;
import com.c99.innovation.dto.statistic.CombiningTypeCountingAndIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class StatisticDAOImpl implements StatisticDAO {

    private EntityManager entityManager;

    @Autowired
    public StatisticDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * This method is used to set the right store procedure calling name to fetch the counting data in each type in each
     * time period(recent months or quarter or years)
     *
     * @param timePeriodOption
     * @return
     */
    @Override
    public List<TypesCountingInCertainTime> getTheCountingStatisticInTimePeriods(TimePeriodOption timePeriodOption) {
        StoredProcedureQuery procedureQuery;
        switch (timePeriodOption) {
            case MONTH: {
                procedureQuery = entityManager.createStoredProcedureQuery("statisticInMonths");
                break;
            }
            case QUARTER: {
                procedureQuery = entityManager.createStoredProcedureQuery("statisticInQuarters");
                break;
            }
            case YEAR: {
                procedureQuery = entityManager.createStoredProcedureQuery("statisticInYears");
                break;
            }
            default: {
                return new ArrayList<>();
            }
        }
        return this.fetchingTheCountingStatisticData(procedureQuery);
    }

    /**
     * This method is used to set the right store procedure calling name to fetch the counting data of
     * top contributing project in a certain time (one month or one quarter or one year)
     * in all types or a specific type (can set the limit to fetching the right quantity)
     *
     * @param startDate
     * @param endDate
     * @param typeId
     * @param limit
     * @return
     */
    @Override
    public List<ContributingCounting> getContributingProjectsInACertainTime(LocalDateTime startDate, LocalDateTime endDate, Optional<Long> typeId, int limit) {
        StoredProcedureQuery storedProcedure;
        if (typeId.isEmpty()) {
            storedProcedure = entityManager.createStoredProcedureQuery("topContributingProjectBetweenTwoDateInAllType");
        } else {
            storedProcedure = entityManager.createStoredProcedureQuery("topContributingProjectBetweenTwoDateInOneType");
        }
        return this.fetchAndParseTopDateFromResultList(storedProcedure, startDate, endDate, typeId, limit);
    }

    /**
     * This method is used to set the right store procedure calling name to fetch the counting data of
     * top trending areas in a certain time (one month or one quarter or one year)
     * in all types or a specific type
     *
     * @param startDate
     * @param endDate
     * @param typeId
     * @param limit
     * @return List<ContributingCounting>
     */
    @Override
    public List<ContributingCounting> getTrendingAreasInACertainTime(LocalDateTime startDate, LocalDateTime endDate, Optional<Long> typeId, int limit) {
        StoredProcedureQuery storedProcedure;
        if (typeId.isEmpty()) {
            storedProcedure = entityManager.createStoredProcedureQuery("topTrendingAreaBetweenTwoDateInAllType");
        } else {
            storedProcedure = entityManager.createStoredProcedureQuery("topTrendingAreaBetweenTwoDateInOneType");
        }
        return this.fetchAndParseTopDateFromResultList(storedProcedure, startDate, endDate, typeId, limit);
    }

    /**
     * This method is used to fetch the counting data in each type in each
     * time period (recent months or quarter or years)
     * to see more about the output of database query, let's go to the script.sql descriptions
     *
     * @param procedureQuery
     * @return List<TypesCountingInCertainTime>
     */
    private List<TypesCountingInCertainTime> fetchingTheCountingStatisticData(StoredProcedureQuery procedureQuery) {
        List<TypesCountingInCertainTime> typesCountingList = new ArrayList<>();
        if (procedureQuery.execute()) {

            List<Object[]> resultList = procedureQuery.getResultList();
            for (int currentIndex = 0; currentIndex < resultList.size(); currentIndex++) {
                String type = (String) resultList.get(currentIndex)[2];
                Integer month = (Integer) resultList.get(currentIndex)[0];
                Integer year = (Integer) resultList.get(currentIndex)[1];

                if (type == null) {
                    typesCountingList.add(new TypesCountingInCertainTime(month, year, 0, 0, 0));

                } else {
                    CombiningTypeCountingAndIndex theNextTypeCounting = this.getTheNextTypeCounting(currentIndex, month, year, resultList);
                    typesCountingList.add(theNextTypeCounting.getTypesCounting());
                    currentIndex = theNextTypeCounting.getIndex();
                }
            }
            return typesCountingList;

        } else {
            return new ArrayList<>();
        }
    }

    /**
     * The helper method is used to help fetchingTheCountingStatisticData method
     * fetching the counting value of each type in the current examined months or quarter or year
     *
     * @param currentIndex
     * @param month
     * @param year
     * @param resultList
     * @return
     */
    private CombiningTypeCountingAndIndex getTheNextTypeCounting(int currentIndex, Integer month, Integer year, List<Object[]> resultList) {

        TypesCountingInCertainTime typesCounting = new TypesCountingInCertainTime(month, year);
        boolean flag = true;
        while (flag) {
            String typeValue = (String) resultList.get(currentIndex)[2];
            long countingValue = ((BigInteger) resultList.get(currentIndex)[3]).longValue();
            if (InnovationType.INNOVATION.toString().equalsIgnoreCase(typeValue)) {
                typesCounting.setInnovation(countingValue);
            }
            if (InnovationType.IMPROVEMENT.toString().equalsIgnoreCase(typeValue)) {
                typesCounting.setImprovement(countingValue);
            }
            if (InnovationType.IDEA.toString().equalsIgnoreCase(typeValue)) {
                typesCounting.setIdea(countingValue);
            }
            currentIndex++;
            if (currentIndex < resultList.size()) {
                flag = month.equals(resultList.get(currentIndex)[0]) && year.equals(resultList.get(currentIndex)[1]);
            } else {
                flag = false;
            }
        }
        --currentIndex;
        return new CombiningTypeCountingAndIndex(typesCounting, currentIndex);
    }

    /**
     * This method is used to set the necessary parameters for store procedure calling
     * then convert each value in the result list into java pojo class which stand for project name and its
     * counting contributing value
     * to see more about the output of database query, let's go to the script.sql descriptions
     *
     * @param storedProcedure
     * @param startDate
     * @param endDate
     * @param typeId
     * @param limit
     * @return List<ContributingCounting>
     */
    private List<ContributingCounting> fetchAndParseTopDateFromResultList(StoredProcedureQuery storedProcedure, LocalDateTime startDate, LocalDateTime endDate, Optional<Long> typeId, int limit) {
        storedProcedure.registerStoredProcedureParameter("start_date", LocalDateTime.class, ParameterMode.IN);
        storedProcedure.setParameter("start_date", startDate);
        storedProcedure.registerStoredProcedureParameter("end_date", LocalDateTime.class, ParameterMode.IN);
        storedProcedure.setParameter("end_date", endDate);
        if (!typeId.isEmpty()) {
            storedProcedure.registerStoredProcedureParameter("type_id", Long.class, ParameterMode.IN);
            storedProcedure.setParameter("type_id", typeId.get());
        }
        storedProcedure.registerStoredProcedureParameter("limit_value", Integer.class, ParameterMode.IN);
        storedProcedure.setParameter("limit_value", limit);

        if (storedProcedure.execute()) {
            List<Object[]> resultList = storedProcedure.getResultList();
            return resultList.stream()
                    .filter(fieldData -> fieldData[1] != null)
                    .map(fieldData -> {
                        String projectName = (String) fieldData[0];
                        Long countingValue = ((BigDecimal) fieldData[1]).longValue();
                        return new ContributingCounting(projectName, countingValue);
                    }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
