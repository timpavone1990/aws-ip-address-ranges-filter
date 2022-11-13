package de.timpavone1990.aws_ip_adress_ranges_filter.repositories;

import de.timpavone1990.aws_ip_adress_ranges_filter.clients.AwsIpAddressRangesClient;
import de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter;
import de.timpavone1990.aws_ip_adress_ranges_filter.model.Prefix;
import org.springframework.stereotype.Repository;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class AwsIpAddressRangesRepository {

    private final AwsIpAddressRangesClient client;

    public AwsIpAddressRangesRepository(final AwsIpAddressRangesClient client) {
        this.client = client;
    }

    public Set<Prefix> findIpAddressRangesByRegion(final RegionFilter region) {
        final var allPrefixes = client.getIpAddressRanges().prefixes();
        return region == null || region == RegionFilter.ALL ? allPrefixes : filterByRegion(allPrefixes, region);
    }

    private Set<Prefix> filterByRegion(final Set<Prefix> prefixes, final RegionFilter region) {
        return prefixes.stream()
                .filter(prefix -> prefix.region().startsWith(region.getValue().toLowerCase(Locale.getDefault())))
                .collect(Collectors.toSet());
    }
}
