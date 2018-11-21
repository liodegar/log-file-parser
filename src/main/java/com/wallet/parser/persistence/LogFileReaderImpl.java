package com.wallet.parser.persistence;

import com.wallet.parser.model.WebServerRequest;
import com.wallet.parser.service.exception.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Liodegar on 11/20/2018.
 */
@Repository
public class LogFileReaderImpl implements LogFileReader {

    private static final Logger logger = LoggerFactory.getLogger(LogFileReaderImpl.class);


    /**
     * Eagerly reads the each line of the file and transform it into a WebServerRequest object
     * @param accessLog The absolute access log file path
     * @return List<WebServerRequest> a list of WebServerRequest
     * @throws ParserException if any error is encountered
     */
    @Override
    public List<WebServerRequest> readAllFile(Path accessLog) throws ParserException {
        try {
            List<WebServerRequest> result = new ArrayList<>();
            List<String> lines = Files.readAllLines(accessLog);

            for (String line : lines) {
                String[] values = line.split(Pattern.quote("|"));
                result.add(getWebServerRequest(values));

            }
            return result;

        } catch (Exception e) {
            logger.error("Exception encountered reading the log file", e);
            throw new ParserException("Exception encountered reading the log file", e);
        }
    }

    private WebServerRequest getWebServerRequest(String[] values) {
        WebServerRequest webServerRequest = new WebServerRequest();
        webServerRequest.setDate(LocalDateTime.parse(values[0], WebServerRequest.DATE_LOG_FORMATTER));
        webServerRequest.setIp(values[1]);
        webServerRequest.setRequestMethod(values[2]);
        webServerRequest.setHttpStatus(values[3]);
        webServerRequest.setUserAgent(values[4]);
        return webServerRequest;
    }
}
