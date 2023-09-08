package de.timpavone1990.aws_ip_adress_ranges_filter.clients.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IpV6PrefixTest {

    private final IpV6Prefix ipV6Prefix = new IpV6Prefix("2a05:d07a:a000::/40", "eu-central-1");

    @Test
    void ipPrefix() {
        assertThat(ipV6Prefix.ipPrefix()).isEqualTo("2a05:d07a:a000::/40");
    }

    @Test
    void region() {
        assertThat(ipV6Prefix.region()).isEqualTo("eu-central-1");
    }

    @Test
    void withRegionCode() {
        final var copy = ipV6Prefix.withRegion("us-east-1");
        assertThat(copy.ipPrefix()).isEqualTo("2a05:d07a:a000::/40");
        assertThat(copy.region()).isEqualTo("us-east-1");
    }
}
