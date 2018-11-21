package com.wallet.parser.service;

import com.wallet.parser.model.ParserDuration;
import com.wallet.parser.model.ParserParam;
import com.wallet.parser.model.WebServerRequest;
import com.wallet.parser.persistence.LogFileReader;
import com.wallet.parser.persistence.WebServerRequestRepository;
import com.wallet.parser.service.exception.ParserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.*;


/**
 * Test case for validating the proper functionality of ParserImp
 * Created by Liodegar on 11/20/2018.
 */
@RunWith(SpringRunner.class)
public class ParserImplTest {
    @Mock
    private LogFileReader logFileReader;

    @Mock
    private WebServerRequestRepository repository;

    @Spy
    @InjectMocks
    private ParserImpl systemUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ParserException.class)
    public void testProcessLogFileForInvalidParameter() throws Exception {
        systemUnderTest.processLogFile(null);
    }

    /**
     * Tests the happy path functionality of processLogFile method, ie when all the parameter are sent and everything works as expected
     */
    @Test()
    public void testProcessLogFileHappyPath() throws Exception {
        //given conditions
        URI uri = ClassLoader.getSystemResource("").toURI();
        String mainPath = Paths.get(uri).toString();
        Path path = Paths.get(mainPath ,"access.log");

        ParserParam parserParam = new ParserParam(LocalDateTime.now(), ParserDuration.DAILY, 200, path.toFile().getCanonicalPath());
        List<WebServerRequest> webServerRequests = new ArrayList<>();

        //when
        doReturn(webServerRequests).when(repository).saveAll(anyIterable());

        //validate
        systemUnderTest.processLogFile(parserParam);

        //then
        verify(systemUnderTest, times(1)).logRequest(Matchers.<Map<String, List<WebServerRequest>>>any());
        //then
        verify(systemUnderTest, times(1)).saveAllRequest(Matchers.<Map<String, List<WebServerRequest>>>any());
    }

    /**
     * Tests the happy path functionality of getFinalRecords method for HOURLY, ie when all the parameter are sent and everything works as expected
     */
    @Test()
    public void testGetFinalRecordsHappyPathForHourly() throws Exception {
        //given conditions
        String accessLog = "src/test/resources/access.log";
        ParserParam parserParam = new ParserParam(LocalDateTime.of(2018, 11, 20, 14, 0, 0), ParserDuration.HOURLY, 3, accessLog);
        List<WebServerRequest> webServerRequests = new ArrayList<>();
        String ip1 = "192.168.102.136";
        //Requests for IP "192.168.102.136"
        List<WebServerRequest> listIp1 = new ArrayList<>();
        listIp1.add(createWebServerRequest(ip1, LocalDateTime.of(2018, 11, 20, 14, 0)));
        listIp1.add(createWebServerRequest(ip1, LocalDateTime.of(2018, 11, 20, 14, 2)));
        listIp1.add(createWebServerRequest(ip1, LocalDateTime.of(2018, 11, 20, 14, 3)));
        listIp1.add(createWebServerRequest(ip1, LocalDateTime.of(2018, 11, 20, 14, 4)));
        listIp1.add(createWebServerRequest(ip1, LocalDateTime.of(2018, 11, 20, 15, 0)));
        //This should not be included
        WebServerRequest webServerRequestIp1Invalid1 = createWebServerRequest("192.168.102.136", LocalDateTime.of(2018, 11, 20, 15, 5));
        listIp1.add(webServerRequestIp1Invalid1);
        webServerRequests.addAll(listIp1);


        String ip2 = "192.168.102.105";
        //Requests for IP "192.168.102.105"
        List<WebServerRequest> listIp2 = new ArrayList<>();
        listIp2.add(createWebServerRequest(ip2, LocalDateTime.of(2018, 11, 20, 14, 0)));
        listIp2.add(createWebServerRequest(ip2, LocalDateTime.of(2018, 11, 20, 14, 2)));
        listIp2.add(createWebServerRequest(ip2, LocalDateTime.of(2018, 11, 20, 14, 3)));
        listIp2.add(createWebServerRequest(ip2, LocalDateTime.of(2018, 11, 20, 15, 0)));
        //These should not be included
        WebServerRequest webServerRequestIp2Invalid1 = createWebServerRequest(ip2, LocalDateTime.of(2018, 11, 20, 15, 5));
        WebServerRequest webServerRequestIp2Invalid2 = createWebServerRequest(ip2, LocalDateTime.of(2018, 11, 20, 15, 7));
        listIp2.add(webServerRequestIp2Invalid1);
        listIp2.add(webServerRequestIp2Invalid2);
        webServerRequests.addAll(listIp2);

        String ip3 = "192.168.102.99";
        //Requests for IP "192.168.102.99". This IP doesn't reach the threshold, so it will not be included
        webServerRequests.add(createWebServerRequest(ip3, LocalDateTime.of(2018, 11, 20, 14, 0)));
        webServerRequests.add(createWebServerRequest(ip3, LocalDateTime.of(2018, 11, 20, 14, 2)));

        //validate
        Map<String, List<WebServerRequest>> result = systemUnderTest.getFinalRecords(webServerRequests, parserParam);

        //then
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(5, result.get(ip1).size());
        listIp1.remove(webServerRequestIp1Invalid1);
        assertEquals(listIp1, result.get(ip1));

        assertEquals(4, result.get(ip2).size());
        listIp2.remove(webServerRequestIp2Invalid1);
        listIp2.remove(webServerRequestIp2Invalid2);
        assertEquals(listIp2, result.get(ip2));

        assertNull(result.get(ip3));
    }

    /**
     * Tests the happy path functionality of getFinalRecords method for DAILY, ie when all the parameter are sent and everything works as expected
     */
    @Test()
    public void testGetFinalRecordsHappyPathForDaily() throws Exception {
        //given conditions
        String accessLog = "src/test/resources/access.log";
        ParserParam parserParam = new ParserParam(LocalDateTime.of(2018, 11, 20, 14, 0, 0), ParserDuration.DAILY, 3, accessLog);
        List<WebServerRequest> webServerRequests = new ArrayList<>();
        String ip1 = "192.168.102.136";
        //Requests for IP "192.168.102.136"
        List<WebServerRequest> listIp1 = new ArrayList<>();
        listIp1.add(createWebServerRequest(ip1, LocalDateTime.of(2018, 11, 20, 14, 0)));
        listIp1.add(createWebServerRequest(ip1, LocalDateTime.of(2018, 11, 20, 18, 2)));
        listIp1.add(createWebServerRequest(ip1, LocalDateTime.of(2018, 11, 20, 20, 3)));
        listIp1.add(createWebServerRequest(ip1, LocalDateTime.of(2018, 11, 20, 23, 4)));
        listIp1.add(createWebServerRequest(ip1, LocalDateTime.of(2018, 11, 21, 13, 0)));
        //This should not be included
        WebServerRequest webServerRequestIp1Invalid1 = createWebServerRequest("192.168.102.136", LocalDateTime.of(2018, 11, 21, 15, 5));
        listIp1.add(webServerRequestIp1Invalid1);
        webServerRequests.addAll(listIp1);


        String ip2 = "192.168.102.105";
        //Requests for IP "192.168.102.105"
        List<WebServerRequest> listIp2 = new ArrayList<>();
        listIp2.add(createWebServerRequest(ip2, LocalDateTime.of(2018, 11, 20, 14, 0)));
        listIp2.add(createWebServerRequest(ip2, LocalDateTime.of(2018, 11, 20, 14, 2)));
        listIp2.add(createWebServerRequest(ip2, LocalDateTime.of(2018, 11, 21, 13, 3)));
        listIp2.add(createWebServerRequest(ip2, LocalDateTime.of(2018, 11, 21, 11, 0)));
        //These should not be included
        WebServerRequest webServerRequestIp2Invalid1 = createWebServerRequest(ip2, LocalDateTime.of(2018, 11, 19, 15, 5));
        WebServerRequest webServerRequestIp2Invalid2 = createWebServerRequest(ip2, LocalDateTime.of(2018, 11, 21, 15, 7));
        listIp2.add(webServerRequestIp2Invalid1);
        listIp2.add(webServerRequestIp2Invalid2);
        webServerRequests.addAll(listIp2);

        String ip3 = "192.168.102.99";
        //Requests for IP "192.168.102.99". This IP doesn't reach the threshold, so it will not be included
        webServerRequests.add(createWebServerRequest(ip3, LocalDateTime.of(2018, 11, 20, 14, 0)));
        webServerRequests.add(createWebServerRequest(ip3, LocalDateTime.of(2018, 11, 21, 10, 2)));

        //validate
        Map<String, List<WebServerRequest>> result = systemUnderTest.getFinalRecords(webServerRequests, parserParam);

        //then
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(5, result.get(ip1).size());
        listIp1.remove(webServerRequestIp1Invalid1);
        assertEquals(listIp1, result.get(ip1));

        assertEquals(4, result.get(ip2).size());
        listIp2.remove(webServerRequestIp2Invalid1);
        listIp2.remove(webServerRequestIp2Invalid2);
        assertEquals(listIp2, result.get(ip2));

        assertNull(result.get(ip3));
    }


    private WebServerRequest createWebServerRequest(String ip, LocalDateTime date) {
        WebServerRequest webServerRequest = new WebServerRequest();
        webServerRequest.setIp(ip);
        webServerRequest.setDate(date);
        webServerRequest.setRequestMethod("GET / HTTP/1.1");
        webServerRequest.setHttpStatus("200");
        webServerRequest.setUserAgent("Mozilla/5.0 (Windows NT 6.3;");
        return webServerRequest;


    }
}