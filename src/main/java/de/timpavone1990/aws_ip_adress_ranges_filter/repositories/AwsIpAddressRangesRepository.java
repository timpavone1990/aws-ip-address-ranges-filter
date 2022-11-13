package de.timpavone1990.aws_ip_adress_ranges_filter.repositories;

import de.timpavone1990.aws_ip_adress_ranges_filter.clients.AwsIpAddressRangesClient;
import de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter;
import de.timpavone1990.aws_ip_adress_ranges_filter.model.Prefix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class AwsIpAddressRangesRepository {

    private final Logger logger = LoggerFactory.getLogger(AwsIpAddressRangesRepository.class);
    private final AwsIpAddressRangesClient client;

    public AwsIpAddressRangesRepository(final AwsIpAddressRangesClient client) {
        this.client = client;
    }

    public Set<Prefix> findIpAddressRangesByRegion(final RegionFilter region) {
        logger.debug("Fetching AWS IP address ranges from the datasource");
        final var allPrefixes = client.getIpAddressRanges().prefixes();
        logger.debug("Fetched AWS IP address ranges from the datasource: {}", allPrefixes);
        if (region == null || region == RegionFilter.ALL) {
            logger.debug("Returning all fetched ranges, because requested region={}", region);
            return allPrefixes;
        } else {
            logger.debug("Filtering fetched ranges by region={}", region);
            final var prefixes = filterByRegion(allPrefixes, region);
            logger.debug("Returning filtered ranges: {}", prefixes);
            return prefixes;
        }
    }

    private Set<Prefix> filterByRegion(final Set<Prefix> prefixes, final RegionFilter region) {
        return prefixes.stream()
                .filter(prefix -> prefix.region().startsWith(region.getValue().toLowerCase(Locale.getDefault())))
                .collect(Collectors.toSet());
    }
}
