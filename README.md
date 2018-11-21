# Web server log file parser

This API provides a parser in Java that parses a web server access log file, loads the log to MySQL and checks if a given IP makes more than
a certain number of requests for the given duration.

## Design considerations
- This application was designed as a multilayer Java application.

- The technology stack used encompasses Java 8, Spring Boot 2.1.0. Hibernate 5.1 and MySQL 8.0

- Logging: SLF4J was used to decouple from any specific implementation. The underlying logging is provided by Log4J.

- Exception handling: all the errors and exceptions are gracefully managed.

- Documentation: all the core classes are intradocumented (Javadocs).

- No hard coded values. All the config properties are defined in the `application.properties` file

- Test cases: core classes are covered by unit test cases.

## Getting Started

In order to start the application and run the parser, you should first build it:

`mvn clean install`

and then execute from a command line the following instruction:

`mvn spring-boot:run -Dspring-boot.run.arguments="--startDate=2017-01-01.13:00:00,--duration=hourly,--threshold=200,--accesslog=/test/resources/access.log"`

All the four parameter: startDate(yyyy-MM-dd.HH:mm:ss), duration(hourly, daily),  threshold (integer) and accesslog (log file path) are required. Failing in giving these values will cause an exception.

After this command, the parser will execute the following steps:

1. Read the log file in a eager way.

2. Parse each line into a Java object.

3. Filter the retrieved data.

4. Log all the result records. This functionality sends these records to the STDOUT console, but if
it is required to redirect them to a specific file, these can be easily achieve by adding another appender in the lo4j.properties file.

5. Save all the result records in the defined MySQL DB instance.

### Prerequisites

A version of JDK 8 or higher should be installed in order to run the application.

The MySQL DB instance should be running and configured with the parser schema.


### Running the web services

## More info

Every time the parser is run, all the database artifacts are created again.


## MySQL schema used for the log data
The schema to define the database artifacts is located at src/main/resources/schema.sql

## SQL queries for SQL test
The queries file for testing purposes is located at src/main/resources/queries.sql

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management


## Authors

* **Liodegar Bracamonte** - *Initial work* - [liodegar@gmail.com)


## License

Apache License 2.0.

## Acknowledgments

* To the all open source software contributors.


