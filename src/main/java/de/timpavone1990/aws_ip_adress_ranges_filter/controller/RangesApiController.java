package de.timpavone1990.aws_ip_adress_ranges_filter.controller;

import de.timpavone1990.aws_ip_adress_ranges_filter.generated.api.RangesApi;
import de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter;
import de.timpavone1990.aws_ip_adress_ranges_filter.repositories.AwsRegionRepository;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@Timed
public class RangesApiController implements RangesApi {

    private final Logger logger = LoggerFactory.getLogger(RangesApiController.class);
    private final AwsRegionRepository awsRegionRepository;
    private final AwsIpAddressRangesResponseRenderer responseRenderer;

    public RangesApiController(final AwsRegionRepository awsRegionRepository, final AwsIpAddressRangesResponseRenderer responseRenderer) {
        this.awsRegionRepository = awsRegionRepository;
        this.responseRenderer = responseRenderer;
    }

    @Override
    public ResponseEntity<String> findAwsIpAddressRangesByRegion(final RegionFilter regionFilter) {
        logger.info("Client requested ip address ranges with regionFilter={}.", regionFilter);
        final var regions = awsRegionRepository.findRegions(regionFilter);
        final var response = responseRenderer.renderRegions(regions);
        logger.debug("Collected ip address ranges for regionFilter={}: {}", regionFilter, response);
        return ResponseEntity.of(Optional.of(response));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleInvalidRegionFilter(final HttpServletResponse response) throws IOException {
        final var validRegionFilters = Arrays.stream(RegionFilter.values()).map(RegionFilter::getValue).collect(Collectors.joining(", "));
        final var message = "Parameter region must be empty or one of: " + validRegionFilters;
        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(message);
    }
}
