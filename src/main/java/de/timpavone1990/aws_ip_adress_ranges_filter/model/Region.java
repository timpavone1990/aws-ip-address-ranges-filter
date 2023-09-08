package de.timpavone1990.aws_ip_adress_ranges_filter.model;

import java.util.Set;

public record Region(String code, Set<String> ipAddressRanges) {
}
