package de.timpavone1990.aws_ip_adress_ranges_filter.clients.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IpV4PrefixTest {

    private final IpV4Prefix ipV4Prefix = new IpV4Prefix("1.2.3.4/11", "eu-central-1");

    @Test
    void ipPrefix() {
        assertThat(ipV4Prefix.ipPrefix()).isEqualTo("1.2.3.4/11");
    }

    @Test
    void region() {
        assertThat(ipV4Prefix.region()).isEqualTo("eu-central-1");
    }

    @Test
    void withRegionCode() {
        final var copy = ipV4Prefix.withRegion("us-east-1");
        assertThat(copy.ipPrefix()).isEqualTo("1.2.3.4/11");
        assertThat(copy.region()).isEqualTo("us-east-1");

    }
}
