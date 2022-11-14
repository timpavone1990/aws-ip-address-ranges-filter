package de.timpavone1990.aws_ip_adress_ranges_filter.controller.model;

import de.timpavone1990.aws_ip_adress_ranges_filter.model.Region;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.flatMapping;
import static java.util.stream.Collectors.toSet;

public record AwsIpAddressRangesResponse(Set<AwsIpAddressRangesResponseItem> responseItems) {

    public static AwsIpAddressRangesResponse fromRegions(final Set<Region> regions) {
        final var responseItems = regions.stream()
                .map(AwsIpAddressRangesResponseItem::createItemsFromRegion)
                .collect(flatMapping(Collection::stream, toSet()));
        return new AwsIpAddressRangesResponse(responseItems);
    }

    @Override
    public String toString() {
        return responseItems.stream()
                .map(AwsIpAddressRangesResponseItem::toString)
                .collect(Collectors.joining("\n"));
    }
}
