import http from "k6/http";

export const options = {
    vus: 20,
    duration: "30s",
    thresholds: {
        "http_req_duration": ["p(95)<1000"],
        "http_req_failed": ["rate<0.01"]
    },
};

const regions = [
    "ALL",
    "EU",
    "US",
    "AP",
    "CN",
    "SA",
    "AF",
    "CA"
];

export default function () {
    const region = regions[Math.floor(Math.random() * regions.length)];
    http.get(`http://localhost:8080/v1/aws/ip-address-ranges?region=${region}`);
}

