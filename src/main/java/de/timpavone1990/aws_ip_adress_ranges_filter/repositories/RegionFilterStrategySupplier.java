package de.timpavone1990.aws_ip_adress_ranges_filter.repositories;

import de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter;
import org.springframework.stereotype.Component;

import static de.timpavone1990.aws_ip_adress_ranges_filter.repositories.RegionFilterStrategy.MATCH_ALL_STRATEGY;
import static de.timpavone1990.aws_ip_adress_ranges_filter.repositories.RegionFilterStrategy.REGION_MATCH_STRATEGY;

@Component
public class RegionFilterStrategySupplier {

    public RegionFilterStrategy provideRegionFilterStrategy(final RegionFilter region) {
        if (region == null) {
            return MATCH_ALL_STRATEGY;
        }
        return switch (region) {
            case ALL -> MATCH_ALL_STRATEGY;
            default -> REGION_MATCH_STRATEGY;
        };
    }
}
