package de.timpavone1990.aws_ip_adress_ranges_filter.controller;

import de.timpavone1990.aws_ip_adress_ranges_filter.model.Region;
import de.timpavone1990.aws_ip_adress_ranges_filter.repositories.AwsRegionRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RangesApiController.class)
@Import(AwsIpAddressRangesResponseRenderer.class)
class RangesApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AwsRegionRepository repository;

    @Nested
    class FindAwsIpAddressRangesByRegion {

        @Test
        void testIpAddressRangesResponseBodyFormat() throws Exception {
            final var regions = Set.of(
                    new Region("eu-central-1", Set.of("52.219.170.0/23")),
                    new Region("eu-central-2", Set.of("52.219.160.0/23")),
                    new Region("us-east-1", Set.of("52.219.168.0/24"))
            );
            when(repository.findRegions(any())).thenReturn(regions);
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
                    .andExpect(content().string("Parameter region must be empty or one of: ALL, EU, US, AP, CN, SA, AF, CA"));
        }
    }
}
