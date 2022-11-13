package de.timpavone1990.aws_ip_adress_ranges_filter.controller.model;

import de.timpavone1990.aws_ip_adress_ranges_filter.controller.model.AwsIpAddressRangesResponse;
import de.timpavone1990.aws_ip_adress_ranges_filter.model.Prefix;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AwsIpAddressRangesResponseTest {

    @Test
    void testFromPrefixes() {
        final var prefixes = Set.of(
                new Prefix("52.219.170.0/23", "eu-central-1"),
                new Prefix("52.219.160.0/23", "eu-central-2"),
                new Prefix("52.219.168.0/24", "us-east-1")
        );
        final var response = AwsIpAddressRangesResponse.fromPrefixes(prefixes);
        final var responseString = response.toString();

        assertThat(responseString.split("\n")).containsExactlyInAnyOrder(
            "EU 52.219.170.0/23", "EU 52.219.160.0/23", "US 52.219.168.0/24"
        );
    }
}
