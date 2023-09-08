package de.timpavone1990.aws_ip_adress_ranges_filter.clients.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IpV6Prefix(
    @JsonProperty("ipv6_prefix") String ipPrefix,
    String region
) implements Prefix {
    @Override
    public String ipPrefix() {
        return ipPrefix;
    }

    @Override
    public String region() {
        return region;
    }

    @Override
    public Prefix withRegionCode(final String regionCode) {
        return new IpV6Prefix(ipPrefix, regionCode);
    }
}
