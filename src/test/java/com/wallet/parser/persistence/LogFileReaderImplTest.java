package com.wallet.parser.persistence;

import com.wallet.parser.model.WebServerRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the proper functionality of LogFileReaderImpl class
 * Created by Liodegar on 11/20/2018.
 */
@RunWith(SpringRunner.class)
public class LogFileReaderImplTest {

    @InjectMocks
    private LogFileReaderImpl systemUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testReadAllFile() throws Exception {
        URI uri = ClassLoader.getSystemResource("com/wallet/parser/persistence").toURI();
        String mainPath = Paths.get(uri).toString();
        Path path = Paths.get(mainPath ,"access_test.log");

        //validate
        List<WebServerRequest> result = systemUnderTest.readAllFile(path);

        //then
        assertNotNull(result);
        assertEquals(11, result.size());
    }
}