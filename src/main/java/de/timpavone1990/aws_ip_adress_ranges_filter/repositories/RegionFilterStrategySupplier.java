package de.timpavone1990.aws_ip_adress_ranges_filter.repositories;

import de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter;
import org.springframework.stereotype.Component;

import static de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter.ALL;
import static de.timpavone1990.aws_ip_adress_ranges_filter.repositories.RegionFilterStrategy.MATCH_ALL_STRATEGY;
import static de.timpavone1990.aws_ip_adress_ranges_filter.repositories.RegionFilterStrategy.REGION_MATCH_STRATEGY;

@Component
public class RegionFilterStrategySupplier {

    public RegionFilterStrategy supplyRegionFilterStrategy(final RegionFilter region) {
        return region == null || region == ALL ? MATCH_ALL_STRATEGY : REGION_MATCH_STRATEGY;
    }
}
