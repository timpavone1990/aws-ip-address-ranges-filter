package de.timpavone1990.aws_ip_adress_ranges_filter.clients.model;

import java.util.Set;

public record AwsIpAddressRangesClientResponse(Set<Prefix> prefixes) {

}
