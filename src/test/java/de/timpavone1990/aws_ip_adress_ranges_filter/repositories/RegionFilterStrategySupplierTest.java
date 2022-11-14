package de.timpavone1990.aws_ip_adress_ranges_filter.repositories;

import de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter.ALL;
import static de.timpavone1990.aws_ip_adress_ranges_filter.repositories.RegionFilterStrategy.MATCH_ALL_STRATEGY;
import static de.timpavone1990.aws_ip_adress_ranges_filter.repositories.RegionFilterStrategy.REGION_MATCH_STRATEGY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

class RegionFilterStrategySupplierTest {

    private final RegionFilterStrategySupplier regionFilterStrategySupplier = new RegionFilterStrategySupplier();

    @Test
    void testRegionNull() {
        assertThat(regionFilterStrategySupplier.supplyRegionFilterStrategy(null)).isEqualTo(MATCH_ALL_STRATEGY);
    }

    @Test
    void testRegionALL() {
        assertThat(regionFilterStrategySupplier.supplyRegionFilterStrategy(ALL)).isEqualTo(MATCH_ALL_STRATEGY);
    }

    @ParameterizedTest
    @EnumSource(names = "ALL", mode = EXCLUDE)
    void testRegionsExceptAll(final RegionFilter region) {
        assertThat(regionFilterStrategySupplier.supplyRegionFilterStrategy(region)).isEqualTo(REGION_MATCH_STRATEGY);
    }
}
