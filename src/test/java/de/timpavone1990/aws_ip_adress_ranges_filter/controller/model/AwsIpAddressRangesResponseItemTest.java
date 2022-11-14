package de.timpavone1990.aws_ip_adress_ranges_filter.controller.model;

import de.timpavone1990.aws_ip_adress_ranges_filter.model.Region;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AwsIpAddressRangesResponseItemTest {

    @Test
    void testRegionNull() {
        assertThatThrownBy(() -> AwsIpAddressRangesResponseItem.createItemsFromRegion(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testUnsupportedRegionCodeFormat() {
        assertThatThrownBy(() -> AwsIpAddressRangesResponseItem.createItemsFromRegion(new Region("abc-123", Set.of("1.2.3.4/21")))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testCreateItemsFromRegion() {
        final var items = AwsIpAddressRangesResponseItem.createItemsFromRegion(new Region("eu-abc-123", Set.of("1.2.3.4/21", "4.5.6.7/23")));
        assertThat(items).hasSize(2);

        final var sortedItems = items.stream()
                .sorted(Comparator.comparing(AwsIpAddressRangesResponseItem::ipAddressRange))
                .toList();

        assertThat(sortedItems.get(0)).isEqualTo(new AwsIpAddressRangesResponseItem("EU", "1.2.3.4/21"));
        assertThat(sortedItems.get(0).toString()).isEqualTo("EU 1.2.3.4/21");

        assertThat(sortedItems.get(1)).isEqualTo(new AwsIpAddressRangesResponseItem("EU", "4.5.6.7/23"));
        assertThat(sortedItems.get(1).toString()).isEqualTo("EU 4.5.6.7/23");
    }

    @Test
    void testFourPartsRegionCode() {
        final var items = AwsIpAddressRangesResponseItem.createItemsFromRegion(new Region("us-gov-east-1", Set.of("1.2.3.4/21", "4.5.6.7/23")));
        assertThat(items).hasSize(2);
    }
}
