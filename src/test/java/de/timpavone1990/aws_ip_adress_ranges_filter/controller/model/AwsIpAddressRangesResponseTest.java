package de.timpavone1990.aws_ip_adress_ranges_filter.controller.model;

import de.timpavone1990.aws_ip_adress_ranges_filter.model.Region;
import de.timpavone1990.aws_ip_adress_ranges_filter.model.RegionCode;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AwsIpAddressRangesResponseTest {

    @Test
    void testFromRegions() {
        final var regions = Set.of(
                new Region(RegionCode.EU_CENTRAL_1, Set.of("52.219.170.0/23", "53.219.170.0/23")),
                new Region(RegionCode.EU_CENTRAL_2, Set.of("52.219.160.0/23")),
                new Region(RegionCode.US_EAST_1, Set.of("52.219.168.0/24"))
        );

        final var response = AwsIpAddressRangesResponse.fromRegions(regions);
        final var responseString = response.toString();

        assertThat(responseString.lines()).containsExactlyInAnyOrder(
            "EU 52.219.170.0/23", "EU 53.219.170.0/23", "EU 52.219.160.0/23", "US 52.219.168.0/24"
        );
    }
}
