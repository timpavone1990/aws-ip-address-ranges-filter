package de.timpavone1990.aws_ip_adress_ranges_filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AwsIpAddressRangesFilterApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetALLIpAddressRanges() {
        final var responseEntity = restTemplate.getForEntity("/v1/aws/ip-address-ranges?region=ALL", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);

        final var body = responseEntity.getBody();
        assertThat(body).isNotBlank();
        assertThat(body.lines()).allMatch(line -> line.matches("^(.{2} (.*?)/\\d{1,3})$"));
    }

    @Test
    void testGetEUIpAddressRanges() {
        final var responseEntity = restTemplate.getForEntity("/v1/aws/ip-address-ranges?region=EU", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);

        final var body = responseEntity.getBody();
        assertThat(body).isNotBlank();
        assertThat(body.lines()).allMatch(line -> line.matches("^(EU (.*?)/\\d{1,3})$"));
    }
}
