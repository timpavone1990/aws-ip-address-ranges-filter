package de.timpavone1990.aws_ip_adress_ranges_filter.clients;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    classes = {
        AwsIpAddressRangesClient.class,
        FeignTestConfiguration.class,
        FeignAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class
    },
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "clients.aws.ip-address-ranges.url=http://localhost:${wiremock.server.port}"
    }
)
@AutoConfigureWireMock(port = 0)
class AwsIpAddressRangesClientTest {

    @Autowired
    private AwsIpAddressRangesClient client;

    @Test
    void testGetIpAddressRanges() {
        WireMock.stubFor(
            WireMock.get("/ip-ranges.json")
                .willReturn(
                    WireMock.aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                {
                                  "syncToken": "1668133989",
                                  "createDate": "2022-11-11-02-33-09",
                                  "prefixes": [
                                    {
                                      "ip_prefix": "3.2.34.0/26",
                                      "region": "af-south-1",
                                      "service": "AMAZON",
                                      "network_border_group": "af-south-1"
                                    },
                                    {
                                      "ip_prefix": "3.5.140.0/22",
                                      "region": "ap-northeast-2",
                                      "service": "AMAZON",
                                      "network_border_group": "ap-northeast-2"
                                    },
                                    {
                                      "ip_prefix": "13.34.37.64/27",
                                      "region": "ap-southeast-4",
                                      "service": "AMAZON",
                                      "network_border_group": "ap-southeast-4"
                                    }
                                  ]
                                }
                                """)
                )
        );
        final var ipAddressRanges = client.getIpAddressRanges();
        assertThat(ipAddressRanges).isNotNull();
        assertThat(ipAddressRanges.prefixes()).isNotNull();
        assertThat(ipAddressRanges.prefixes()).as("Check the amount of prefixes in the AWS IP address ranges.").hasSize(3);
    }
}
