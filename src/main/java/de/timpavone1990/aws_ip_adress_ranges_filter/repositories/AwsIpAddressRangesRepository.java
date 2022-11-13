package de.timpavone1990.aws_ip_adress_ranges_filter.repositories;

import de.timpavone1990.aws_ip_adress_ranges_filter.clients.AwsIpAddressRangesClient;
import de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter;
import de.timpavone1990.aws_ip_adress_ranges_filter.model.Prefix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class AwsIpAddressRangesRepository {

    private final Logger logger = LoggerFactory.getLogger(AwsIpAddressRangesRepository.class);
    private final AwsIpAddressRangesClient client;
    private final RegionFilterStrategySupplier regionFilterStrategySupplier;

    public AwsIpAddressRangesRepository(final AwsIpAddressRangesClient client, final RegionFilterStrategySupplier regionFilterStrategySupplier) {
        this.client = client;
        this.regionFilterStrategySupplier = regionFilterStrategySupplier;
    }

    public Set<Prefix> findIpAddressRangesByRegion(final RegionFilter region) {
        logger.debug("Fetching AWS IP address ranges from the datasource");
        final var allPrefixes = client.getIpAddressRanges().prefixes();
        logger.debug("Fetched AWS IP address ranges from the datasource: {}", allPrefixes);
        final var regionFilterStrategy = regionFilterStrategySupplier.provideRegionFilterStrategy(region);
        logger.debug("Using region filter strategy: {}", regionFilterStrategy.name());
        final var filteredPrefixes = regionFilterStrategy.apply(allPrefixes, region);
        logger.debug("Returning filtered ranges: {}", filteredPrefixes);
        return filteredPrefixes;
    }
}
