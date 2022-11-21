package de.timpavone1990.aws_ip_adress_ranges_filter.repositories;

import de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.Prefix;
import de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter;
import de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.IpV4Prefix;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.EU_CENTRAL_1;
import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.EU_CENTRAL_2;
import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.GLOBAL;
import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.US_EAST_1;
import static de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter.EU;
import static de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter.US;
import static de.timpavone1990.aws_ip_adress_ranges_filter.repositories.RegionFilterStrategy.MATCH_ALL_STRATEGY;
import static de.timpavone1990.aws_ip_adress_ranges_filter.repositories.RegionFilterStrategy.REGION_MATCH_STRATEGY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RegionFilterStrategyTest {

    private final Stream<Prefix> prefixes = Stream.of(
            new IpV4Prefix("52.219.170.0/23", EU_CENTRAL_1),
            new IpV4Prefix("52.219.160.0/23", EU_CENTRAL_2),
            new IpV4Prefix("52.219.168.0/24", US_EAST_1),
            new IpV4Prefix("1.2.4.5/21", GLOBAL)
    );

    @Nested
    class MatchAllStrategy {

        @ParameterizedTest
        @NullSource
        @EnumSource
        void testStrategy(final RegionFilter region) {
            assertThat(MATCH_ALL_STRATEGY.apply(prefixes, region)).isEqualTo(prefixes);
        }
    }

    @Nested
    class MatchRegionStrategy {

        @Test
        void testRegionNull() {
            assertThatThrownBy(() -> REGION_MATCH_STRATEGY.apply(prefixes, null)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void testFilterByRegionEU() {
            final Set<Prefix> filteredIpAddressRanges = REGION_MATCH_STRATEGY.apply(prefixes, EU).collect(Collectors.toSet());
            assertThat(filteredIpAddressRanges).hasSize(3);
        }

        @Test
        void testFilterByRegionUS() {
            final Set<Prefix> filteredIpAddressRanges = REGION_MATCH_STRATEGY.apply(prefixes, US).collect(Collectors.toSet());
            assertThat(filteredIpAddressRanges).hasSize(2);
        }
    }
}
