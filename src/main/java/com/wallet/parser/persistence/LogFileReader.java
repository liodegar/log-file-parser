package com.wallet.parser.persistence;

import com.wallet.parser.model.WebServerRequest;
import com.wallet.parser.service.exception.ParserException;

import java.nio.file.Path;
import java.util.List;

/**
 * Defines the contract to read the log file
 * Created by Liodegar on 11/20/2018.
 */
public interface LogFileReader {

    /**
     * Eagerly reads the each line of the file and transform it into a WebServerRequest object
     * @param  accessLog The absolute access log file path
     * @return List<WebServerRequest> a list of WebServerRequest
     * @throws ParserException if any error is encountered
     */
    List<WebServerRequest> readAllFile(Path accessLog) throws ParserException;
}
