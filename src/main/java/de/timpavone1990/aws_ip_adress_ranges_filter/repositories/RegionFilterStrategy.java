package de.timpavone1990.aws_ip_adress_ranges_filter.repositories;

import de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.Prefix;
import de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.RegionCode.GLOBAL;
import static java.util.Locale.ROOT;

public enum RegionFilterStrategy {

    REGION_MATCH_STRATEGY((prefixes, regionFilter) -> {
        if (regionFilter == null) {
            throw new IllegalArgumentException("Parameter regionFilter must not be null in REGION_MATCH_STRATEGY");
        }

        return prefixes
            .filter(prefix -> prefix.region() == GLOBAL ||
                    prefix.region().getCode().toLowerCase(ROOT)
                            .startsWith(regionFilter.getValue().toLowerCase(ROOT)));
    }),

    MATCH_ALL_STRATEGY((prefixes, region) -> prefixes);

    private final BiFunction<Stream<Prefix>, RegionFilter, Stream<Prefix>> filter;

    RegionFilterStrategy(final BiFunction<Stream<Prefix>, RegionFilter, Stream<Prefix>> filter) {
        this.filter = filter;
    }

    public Stream<Prefix> apply(final Stream<Prefix> ipAddressRanges, final RegionFilter region) {
        return filter.apply(ipAddressRanges, region);
    }
}
