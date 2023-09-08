package de.timpavone1990.aws_ip_adress_ranges_filter.clients.model;

import org.junit.jupiter.api.Test;

import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.EU_CENTRAL_1;
import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.US_EAST_1;
import static org.assertj.core.api.Assertions.assertThat;

class IpV6PrefixTest {

    private final IpV6Prefix ipV6Prefix = new IpV6Prefix("2a05:d07a:a000::/40", EU_CENTRAL_1.getCode());

    @Test
    void ipPrefix() {
        assertThat(ipV6Prefix.ipPrefix()).isEqualTo("2a05:d07a:a000::/40");
    }

    @Test
    void region() {
        assertThat(ipV6Prefix.region()).isEqualTo(EU_CENTRAL_1.getCode());
    }

    @Test
    void withRegionCode() {
        final var copy = ipV6Prefix.withRegionCode(US_EAST_1.getCode());
        assertThat(copy.ipPrefix()).isEqualTo("2a05:d07a:a000::/40");
        assertThat(copy.region()).isEqualTo(US_EAST_1.getCode());
    }
}
