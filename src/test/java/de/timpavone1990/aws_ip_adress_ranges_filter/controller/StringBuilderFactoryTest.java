package de.timpavone1990.aws_ip_adress_ranges_filter.controller;

import de.timpavone1990.aws_ip_adress_ranges_filter.model.Region;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static de.timpavone1990.aws_ip_adress_ranges_filter.model.RegionCode.EU_CENTRAL_1;
import static de.timpavone1990.aws_ip_adress_ranges_filter.model.RegionCode.EU_CENTRAL_2;
import static org.assertj.core.api.Assertions.assertThat;

public class StringBuilderFactoryTest {
    @Test
    void createStringBuilderWithAdequateCapacity() {
        final var regions = Set.of(
                new Region(EU_CENTRAL_1, Set.of("111.222.333.444/11", "111.222.333.555/12")),
                new Region(EU_CENTRAL_2, Set.of("111.222.333.666/13"))
        );
        final var stringBuilderFactory = new AwsIpAddressRangesResponseRenderer.StringBuilderFactory();
        final var awsIpAddressRangesResponseRenderer = new AwsIpAddressRangesResponseRenderer(stringBuilderFactory);
        final var minimalCapacity = awsIpAddressRangesResponseRenderer.renderRegions(regions).length();
        final var actualCapacity = stringBuilderFactory.createStringBuilder(regions).capacity();
        assertThat(actualCapacity).isGreaterThanOrEqualTo(minimalCapacity);
    }
}
