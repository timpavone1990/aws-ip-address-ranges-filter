package de.timpavone1990.aws_ip_adress_ranges_filter.controller.model;

import de.timpavone1990.aws_ip_adress_ranges_filter.controller.model.AwsIpAddressRangesResponseItem;
import de.timpavone1990.aws_ip_adress_ranges_filter.model.Prefix;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AwsIpAddressRangesResponseItemTest {

    @Test
    void testPrefixNull() {
        assertThatThrownBy(() -> AwsIpAddressRangesResponseItem.fromPrefix(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testUnsupportedRegionFormat() {
        assertThatThrownBy(() -> AwsIpAddressRangesResponseItem.fromPrefix(new Prefix("1.2.3.4/21", "abc-123"))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testFromPrefix() {
        final var item = AwsIpAddressRangesResponseItem.fromPrefix(new Prefix("1.2.3.4/21", "eu-abc-123"));
        assertThat(item.region()).isEqualTo("EU");
        assertThat(item.ipAddressRange()).isEqualTo("1.2.3.4/21");
        assertThat(item.toString()).isEqualTo("EU 1.2.3.4/21");
    }
}
