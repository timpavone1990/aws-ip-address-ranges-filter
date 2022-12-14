package de.timpavone1990.aws_ip_adress_ranges_filter.repositories;

import de.timpavone1990.aws_ip_adress_ranges_filter.clients.AwsIpAddressRangesClient;
import de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.Prefix;
import de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter;
import de.timpavone1990.aws_ip_adress_ranges_filter.model.Region;
import de.timpavone1990.aws_ip_adress_ranges_filter.model.RegionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Stream;

import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.GLOBAL;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

@Repository
public class AwsRegionRepository {

    private final Logger logger = LoggerFactory.getLogger(AwsRegionRepository.class);
    private final AwsIpAddressRangesClient client;
    private final RegionFilterStrategySupplier regionFilterStrategySupplier;

    public AwsRegionRepository(final AwsIpAddressRangesClient client, final RegionFilterStrategySupplier regionFilterStrategySupplier) {
        this.client = client;
        this.regionFilterStrategySupplier = regionFilterStrategySupplier;
    }

    public Set<Region> findRegions(final RegionFilter region) {
        logger.debug("Fetching AWS IP address ranges from the datasource");
        final var allPrefixes = client.getIpAddressRanges().getAllPrefixes();

        logger.debug("Expanding global prefixes");
        final var expandedPrefixes = expandGlobalPrefixes(allPrefixes);

        final var regionFilterStrategy = regionFilterStrategySupplier.supplyRegionFilterStrategy(region);
        logger.debug("Using region filter strategy: {}", regionFilterStrategy.name());
        final var filteredPrefixes = regionFilterStrategy.apply(expandedPrefixes, region);

        final var ipPrefixesByRegion = filteredPrefixes.collect(groupingBy(Prefix::region, mapping(Prefix::ipPrefix, toSet())));
        final var regions = ipPrefixesByRegion.entrySet().stream()
                .map(entry -> new Region(RegionCode.findByRegionCode(entry.getKey().getCode()), entry.getValue()))
                .collect(toSet());
        logger.debug("Returning filtered regions: {}", regions);
        return regions;
    }

    private Stream<Prefix> expandGlobalPrefixes(final Stream<Prefix> prefixes) {
        return prefixes.flatMap(prefix ->
                GLOBAL != prefix.region()
                        ? Stream.of(prefix)
                        : de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.getRegionsExceptGlobal().stream().map(prefix::withRegionCode)
        );
    }
}
