package de.timpavone1990.aws_ip_adress_ranges_filter.controller;

import de.timpavone1990.aws_ip_adress_ranges_filter.model.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AwsIpAddressRangesResponseRenderer {

    private final Logger logger = LoggerFactory.getLogger(AwsIpAddressRangesResponseRenderer.class);
    private final StringBuilderFactory stringBuilderFactory;

    public AwsIpAddressRangesResponseRenderer(final StringBuilderFactory stringBuilderFactory) {
        this.stringBuilderFactory = stringBuilderFactory;
    }

    public String renderRegions(final Set<Region> regions) {
        logger.debug("Rendering set of regions.");
        if (regions == null) {
            throw new IllegalArgumentException("Parameter regions must not be null.");
        }

        final var responseBuilder = stringBuilderFactory.createStringBuilder(regions);

        for (Region region : regions) {
            for (String ipAddressRange : region.ipAddressRanges()) {
                logger.debug("Rendering region={}, ipAddressRange={}", region.code(), ipAddressRange);
                responseBuilder.append(region.code().getCodeFirstPartUpperCase());
                responseBuilder.append(" ");
                responseBuilder.append(ipAddressRange);
                responseBuilder.append("\n");
            }
        }

        final var renderedRegions = responseBuilder.toString();
        logger.debug("Rendered set of regions to: {}", renderedRegions);
        return renderedRegions;
    }

    @Component
    public static class StringBuilderFactory {

        private final static int MAX_RESPONSE_LINE_LENGTH = 47; // EU 0000:0000:0000:0000:0000:0000:0000:0000/128\n
        private final Logger logger = LoggerFactory.getLogger(StringBuilderFactory.class);

        public StringBuilder createStringBuilder(final Set<Region> regions) {
            final var numberOfLines = calculateNumberOfResponseLines(regions);
            final var capacity = MAX_RESPONSE_LINE_LENGTH * numberOfLines;
            logger.debug("Creating new StringBuilder instance with capacity={}", capacity);
            return new StringBuilder(capacity);
        }

        private Integer calculateNumberOfResponseLines(final Set<Region> regions) {
            logger.debug("Calculating number of response lines for {} regions.", regions.size());
            final var numberOfResponseLines = regions.stream()
                    .map(region -> region.ipAddressRanges().size())
                    .reduce(Integer::sum)
                    .orElse(0);
            logger.debug("Calculated number of response lines: {}", numberOfResponseLines);
            return numberOfResponseLines;
        }
    }
}
