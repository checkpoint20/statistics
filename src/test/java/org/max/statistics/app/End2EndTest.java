package org.max.statistics.app;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.max.statistics.model.Transaction;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Runner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class End2EndTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Test
    public void end2end() throws JSONException {

        ResponseEntity<String> response = getResponse();
        String expected = "{sum:0.0,max:0.0,min:0.0,count:0,avg:0}";
        JSONAssert.assertEquals(expected, response.getBody(), false);

        Transaction t1 = new Transaction(10, System.currentTimeMillis());
        assertEquals(HttpStatus.CREATED, sendRequest(t1));

        Transaction t2 = new Transaction(5, System.currentTimeMillis());
        assertEquals(HttpStatus.CREATED, sendRequest(t2));

        Transaction t3 = new Transaction(3, System.currentTimeMillis());
        assertEquals(HttpStatus.CREATED, sendRequest(t3));

        response = getResponse();
        expected = "{sum:18.0,max:10.0,min:3.0,count:3,avg:6}";
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private HttpStatus sendRequest(Transaction t) {
        HttpEntity<Transaction> entity = new HttpEntity<>(t, headers);
        ResponseEntity<String> res = restTemplate.exchange(
                createURLWithPort("/transactions"),
                HttpMethod.POST, entity, String.class);
        return res.getStatusCode();
    }

    private ResponseEntity<String> getResponse() {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        return restTemplate.exchange(
                createURLWithPort("/statistics"),
                HttpMethod.GET, entity, String.class);
    }
}