package de.timpavone1990.aws_ip_adress_ranges_filter.repositories;

import de.timpavone1990.aws_ip_adress_ranges_filter.clients.AwsIpAddressRangesClient;
import de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter;
import de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.Prefix;
import de.timpavone1990.aws_ip_adress_ranges_filter.model.Region;
import de.timpavone1990.aws_ip_adress_ranges_filter.model.RegionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.GLOBAL;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.partitioningBy;
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
        final var allPrefixes = client.getIpAddressRanges().prefixes();
        logger.debug("Fetched AWS IP address ranges from the datasource: {}", allPrefixes);

        final var expandedPrefixes = expandGlobalPrefixes(allPrefixes);
        logger.debug("Expanded global prefixes. Final prefixes: {}", expandedPrefixes);

        final var regionFilterStrategy = regionFilterStrategySupplier.supplyRegionFilterStrategy(region);
        logger.debug("Using region filter strategy: {}", regionFilterStrategy.name());
        final var filteredPrefixes = regionFilterStrategy.apply(expandedPrefixes, region);

        final var ipPrefixesByRegion = filteredPrefixes.stream().collect(groupingBy(Prefix::region, mapping(Prefix::ipPrefix, toSet())));
        final var regions = ipPrefixesByRegion.entrySet().stream()
                .map(entry -> new Region(RegionCode.findByRegionCode(entry.getKey().getCode()), entry.getValue()))
                .collect(toSet());
        logger.debug("Returning filtered regions: {}", regions);
        return regions;
    }

    private Set<Prefix> expandGlobalPrefixes(final Set<Prefix> prefixes) {
        final Map<Boolean, List<Prefix>> globalAndLocalPrefixes = prefixes.stream().collect(partitioningBy(prefix -> prefix.region().equals(GLOBAL)));
        final List<Prefix> localPrefixes = globalAndLocalPrefixes.get(false);
        final List<Prefix> globalPrefixes = globalAndLocalPrefixes.get(true);

        return de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.getRegionsExceptGlobal()
                .stream().flatMap(region -> globalPrefixes.stream().map(globalPrefix -> new Prefix(globalPrefix.ipPrefix(), region)))
                .collect(collectingAndThen(toSet(), result -> {
                    result.addAll(localPrefixes);
                    return result;
                }));
    }
}
