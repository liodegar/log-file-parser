package com.wallet.parser.service;

import com.wallet.parser.model.ParserDuration;
import com.wallet.parser.model.ParserParam;
import com.wallet.parser.model.WebServerRequest;
import com.wallet.parser.persistence.LogFileReader;
import com.wallet.parser.persistence.WebServerRequestRepository;
import com.wallet.parser.service.exception.ParserException;
import com.wallet.parser.util.ParserCollectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Liodegar on 11/20/2018.
 */
@Service
public class ParserImpl implements Parser {

    private static final Logger logger = LoggerFactory.getLogger(ParserImpl.class);

    @Autowired
    private LogFileReader logFileReader;

    @Autowired
    private WebServerRequestRepository webServerRequestRepository;

    /**
     * Processes the log file by using the given parameter, and then logs and save all the requests
     *
     * @param parserParam The required parameters
     * @throws ParserException if any error is encountered
     */
    @Override
    public void processLogFile(ParserParam parserParam) throws ParserException {
        try {
            if (parserParam == null || !parserParam.isValid()) {
                throw new IllegalArgumentException("The received ParserParam object is invalid");
            }

            Map<String, List<WebServerRequest>> webServerRequests = getFinalRecords(logFileReader.readAllFile(Paths.get(parserParam.getAccessLog())), parserParam);
            logRequest(webServerRequests);
            saveAllRequest(webServerRequests);

        } catch (Exception e) {
            logger.error("Exception encountered with parserParam=" + parserParam, e);
            throw new ParserException("Exception encountered with parserParam=" + parserParam, e);
        }
    }


    protected Map<String, List<WebServerRequest>> getFinalRecords(List<WebServerRequest> webServerRequests, ParserParam parserParam) {
        Predicate<WebServerRequest> predicateByRangeOfTime = getPredicateByRangeOfTime(parserParam);
        Map<String, List<WebServerRequest>> resultByRangeOfTime = webServerRequests.stream()
                .collect(Collectors.groupingBy(WebServerRequest::getIp, ParserCollectors.filtering(predicateByRangeOfTime, Collectors.toList())));

        Map<String, List<WebServerRequest>> result = resultByRangeOfTime.entrySet().stream()
                .filter(x -> x.getValue().size() > parserParam.getThreshold())
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
        return result;
    }


    private Predicate<WebServerRequest> getPredicateByRangeOfTime(ParserParam parserParam) {
        if (ParserDuration.HOURLY.equals(parserParam.getDuration())) {
            LocalTime localTime = parserParam.getStartDate().toLocalTime();
            LocalTime localTimePlusOneHour = localTime.plus(1, ChronoUnit.HOURS);
            return webServerRequest -> webServerRequest.getDate().toLocalTime().equals(localTime)
                    || webServerRequest.getDate().toLocalTime().equals(localTimePlusOneHour)
                    || (webServerRequest.getDate().toLocalTime().isAfter(localTime)
                    && webServerRequest.getDate().toLocalTime().isBefore(localTimePlusOneHour));

        } else if (ParserDuration.DAILY.equals(parserParam.getDuration())) {
            LocalDateTime localDateTime = parserParam.getStartDate();
            LocalDateTime localDateTimePlusOneDay = localDateTime.plus(1, ChronoUnit.DAYS);
            return webServerRequest -> webServerRequest.getDate().equals(localDateTime)
                    || webServerRequest.getDate().equals(localDateTimePlusOneDay)
                    || (webServerRequest.getDate().isAfter(localDateTime)
                    && webServerRequest.getDate().isBefore(localDateTimePlusOneDay));
        }
        return null;
    }


    protected void logRequest(Map<String, List<WebServerRequest>> webServerRequestMap) {
        logger.info("Starting the logging process for " + webServerRequestMap.size() + " IPs");
        for (Map.Entry<String, List<WebServerRequest>> entry : webServerRequestMap.entrySet()) {
            logger.info("Starting the logging process for the following IP=" + entry.getKey());
            entry.getValue().stream().forEach(webServerRequest -> logger.info(String.valueOf(webServerRequest)));
            logger.info("-----------------------------------------------------------------------------------------------");
        }

        logger.info("Logging process finished");
    }


    protected void saveAllRequest(Map<String, List<WebServerRequest>> webServerRequestMap) {
        List<WebServerRequest> webServerRequests = webServerRequestMap.entrySet()
                .stream().map(x -> x.getValue())
                .flatMap(list -> list.stream()).collect(Collectors.toList());
        logger.info("Starting the saving process for " + webServerRequests.size() + " records");
        webServerRequestRepository.saveAll(webServerRequests);
        logger.info("Saving process finished");
    }


}
