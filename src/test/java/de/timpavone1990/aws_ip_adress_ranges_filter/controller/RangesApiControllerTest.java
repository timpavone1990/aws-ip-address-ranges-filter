package de.timpavone1990.aws_ip_adress_ranges_filter.controller;

import de.timpavone1990.aws_ip_adress_ranges_filter.model.Prefix;
import de.timpavone1990.aws_ip_adress_ranges_filter.repositories.AwsIpAddressRangesRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RangesApiController.class)
class RangesApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AwsIpAddressRangesRepository repository;

    @Nested
    class FindAwsIpAddressRangesByRegion {

        @Test
        void testIpAddressRangesResponseBodyFormat() throws Exception {
            final var awsIpAddressRanges = Set.of(
                    new Prefix("52.219.170.0/23", "eu-central-1"),
                    new Prefix("52.219.160.0/23", "eu-central-2"),
                    new Prefix("52.219.168.0/24", "us-east-1")
            );
            when(repository.findIpAddressRangesByRegion(any())).thenReturn(awsIpAddressRanges);
            mockMvc.perform(get("/v1/aws/ip-address-ranges?region=ALL"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/plain;charset=UTF-8"))
                    .andExpect(content().string(containsString("EU 52.219.170.0/23\n")))
                    .andExpect(content().string(containsString("EU 52.219.160.0/23\n")))
                    .andExpect(content().string(containsString("US 52.219.168.0/24\n")));
        }

        @Test
        void testUnknownRegion() throws Exception {
            mockMvc.perform(get("/v1/aws/ip-address-ranges?region=ASD"))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(result -> assertThat(result.getResponse().getErrorMessage()).isEqualTo("Parameter region must be empty or one of: ALL, EU, US, AP, CN, SA, AF, CA"));
        }
    }
}
