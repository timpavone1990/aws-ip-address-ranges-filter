package de.timpavone1990.aws_ip_adress_ranges_filter.clients.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;
import java.util.stream.Stream;

public record AwsIpAddressRangesClientResponse(
    Set<IpV4Prefix> prefixes,
    @JsonProperty("ipv6_prefixes") Set<IpV6Prefix> ipv6Prefixes) {

    /**
     * Gets a unified stream of all ipV4 an ipV6 prefixes.
     * @return a unified stream of all ipV4 an ipV6 prefixes.
     */
    public Stream<Prefix> getAllPrefixes() {
        return Stream.concat(prefixes.stream(), ipv6Prefixes.stream());
    }
}
