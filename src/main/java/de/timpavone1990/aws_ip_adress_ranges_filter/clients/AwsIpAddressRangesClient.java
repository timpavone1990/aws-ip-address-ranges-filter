package de.timpavone1990.aws_ip_adress_ranges_filter.clients;

import de.timpavone1990.aws_ip_adress_ranges_filter.clients.model.AwsIpAddressRangesClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
    name = "aws-ip-address-ranges-client",
    url = "${clients.aws.ip-address-ranges.url}"
)
public interface AwsIpAddressRangesClient {

    @RequestMapping(
        method = RequestMethod.GET,
        value = "/ip-ranges.json"
    )
    AwsIpAddressRangesClientResponse getIpAddressRanges();
}
