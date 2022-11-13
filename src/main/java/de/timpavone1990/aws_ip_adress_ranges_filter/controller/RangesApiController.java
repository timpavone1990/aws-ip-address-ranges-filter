package de.timpavone1990.aws_ip_adress_ranges_filter.controller;

import de.timpavone1990.aws_ip_adress_ranges_filter.generated.api.RangesApi;
import de.timpavone1990.aws_ip_adress_ranges_filter.generated.model.RegionFilter;
import de.timpavone1990.aws_ip_adress_ranges_filter.repositories.AwsIpAddressRangesRepository;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Timed
public class RangesApiController implements RangesApi {

    private final AwsIpAddressRangesRepository awsIpAddressRangesRepository;

    public RangesApiController(final AwsIpAddressRangesRepository awsIpAddressRangesRepository) {
        this.awsIpAddressRangesRepository = awsIpAddressRangesRepository;
    }

    @Override
    public ResponseEntity<String> findAwsIpAddressRangesByRegion(RegionFilter region) {
        final var response = awsIpAddressRangesRepository.findIpAddressRangesByRegion(region).stream()
                .map(entry -> entry.region().substring(0, 2).toUpperCase(Locale.getDefault()) + " " + entry.ipPrefix())
                .collect(Collectors.joining("\n"));
        return ResponseEntity.of(Optional.of(response + "\n"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void handleInvalidRegionFilter(final HttpServletResponse response) throws IOException {
        final var validRegionFilters = Arrays.stream(RegionFilter.values()).map(RegionFilter::getValue).collect(Collectors.joining(", "));
        response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Parameter region must be empty or one of: " + validRegionFilters);
    }
}
