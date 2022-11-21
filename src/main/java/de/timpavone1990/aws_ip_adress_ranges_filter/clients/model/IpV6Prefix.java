package de.timpavone1990.aws_ip_adress_ranges_filter.clients.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IpV6Prefix(
    @JsonProperty("ipv6_prefix") String ipPrefix,
    RegionCode region
) implements Prefix {
    @Override
    public String ipPrefix() {
        return ipPrefix;
    }

    @Override
    public RegionCode region() {
        return region;
    }

    @Override
    public Prefix withRegionCode(final RegionCode regionCode) {
        return new IpV6Prefix(ipPrefix, regionCode);
    }
}
