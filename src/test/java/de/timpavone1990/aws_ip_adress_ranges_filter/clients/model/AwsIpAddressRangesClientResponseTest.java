package de.timpavone1990.aws_ip_adress_ranges_filter.clients.model;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.EU_CENTRAL_1;
import static org.assertj.core.api.Assertions.assertThat;

class AwsIpAddressRangesClientResponseTest {

    @Test
    void testGetAllPrefixes() {
        final var ipV4Prefixes = Set.of(new IpV4Prefix("1.2.3.4/11", EU_CENTRAL_1.getCode()));
        final var ipV6Prefixes = Set.of(
            new IpV6Prefix("2600:1ff2:4000::/40", EU_CENTRAL_1.getCode()),
            new IpV6Prefix("2a05:d07a:a000::/40", EU_CENTRAL_1.getCode())
        );
        final var response = new AwsIpAddressRangesClientResponse(ipV4Prefixes, ipV6Prefixes);

        final var allPrefixes = response.getAllPrefixes().collect(Collectors.toSet());
        assertThat(allPrefixes).hasSize(3);
        assertThat(allPrefixes).containsAll(ipV4Prefixes);
        assertThat(allPrefixes).containsAll(ipV6Prefixes);
    }
}
