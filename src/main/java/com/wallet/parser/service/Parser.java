package com.wallet.parser.service;

import com.wallet.parser.model.ParserParam;
import com.wallet.parser.service.exception.ParserException;

/**
 * Created by Liodegar on 11/20/2018.
 */
public interface Parser {

    /**
     * Processes the log file by using the given parameter, and then logs and save all the requests
     * @param parserParam The required parameters
     * @throws ParserException if any error is encountered
     */
    void processLogFile(ParserParam parserParam) throws ParserException;
}
