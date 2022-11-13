package de.timpavone1990.aws_ip_adress_ranges_filter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Prefix(
    @JsonProperty("ip_prefix") String ipPrefix,
    String region
) {
}