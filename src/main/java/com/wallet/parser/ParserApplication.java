package com.wallet.parser;

import com.wallet.parser.model.ParserDuration;
import com.wallet.parser.model.ParserParam;
import com.wallet.parser.service.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EntityScan(basePackageClasses = {ParserApplication.class, Jsr310JpaConverters.class})
@SpringBootApplication
public class ParserApplication implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ParserApplication.class);

    private static final List<String> REQUIRED_ARGS = Collections.unmodifiableList(Arrays.asList("startDate", "duration", "threshold", "accesslog"));

    @Value("${startDate}")  //--startDate=2017-01-01.13:00:00
    private String startDate;

    @Value("${duration}")  //--duration=hourly
    private String duration;

    @Value("${threshold}")  //--threshold=100
    private String threshold;

    @Value("${accesslog}")  //--accesslog=100
    private String accessLog;

    @Autowired
    private Parser parser;

    public static void main(String... args) throws Exception {
        SpringApplication.run(ParserApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        checkParameters(args);

        logger.info("Application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));
        logger.info("OptionNames: {}", args.getOptionNames());
        args.getOptionNames().stream().forEach(name -> logger.info("arg-" + name + "=" + args.getOptionValues(name)));

        parser.processLogFile(getParserParam());

    }

    private void checkParameters(ApplicationArguments args) {
        if (args == null || args.getSourceArgs() == null || args.getSourceArgs().length == 0) {
            throw new IllegalArgumentException("Required arguments were not received from command line");
        }

        boolean hasAllTheParameters = args.getOptionNames().stream()
                .filter(REQUIRED_ARGS::contains)
                .distinct()
                .limit(REQUIRED_ARGS.size())
                .count() == REQUIRED_ARGS.size();

        if (!hasAllTheParameters) {
            throw new IllegalArgumentException("The required parameters from command line are missing");
        }
    }

    public ParserParam getParserParam() {
        return new ParserParam(LocalDateTime.parse(startDate, ParserParam.DATE_LOG_FORMATTER),
                ParserDuration.getFromCode(duration), getSafeInt(threshold), accessLog);
    }

    private int getSafeInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.info("The received threshold is invalid=" + threshold + " The default value (0) will be used instead.");
            return 0;
        }
    }


}
