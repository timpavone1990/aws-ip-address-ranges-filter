package de.timpavone1990.aws_ip_adress_ranges_filter.clients.model;

import org.junit.jupiter.api.Test;

import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.EU_CENTRAL_1;
import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.US_EAST_1;
import static org.assertj.core.api.Assertions.assertThat;

class IpV4PrefixTest {

    private final IpV4Prefix ipV4Prefix = new IpV4Prefix("1.2.3.4/11", EU_CENTRAL_1.getCode());

    @Test
    void ipPrefix() {
        assertThat(ipV4Prefix.ipPrefix()).isEqualTo("1.2.3.4/11");
    }

    @Test
    void region() {
        assertThat(ipV4Prefix.region()).isEqualTo(EU_CENTRAL_1.getCode());
    }

    @Test
    void withRegionCode() {
        final var copy = ipV4Prefix.withRegionCode(US_EAST_1.getCode());
        assertThat(copy.ipPrefix()).isEqualTo("1.2.3.4/11");
        assertThat(copy.region()).isEqualTo(US_EAST_1.getCode());

    }
}
