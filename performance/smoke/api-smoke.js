import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 1,
  iterations: 3,

  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<3000'],
  },
};

export default function () {
  const baseUrl = __ENV.ALPACA_BASE_URL;
  const apiKey = __ENV.ALPACA_API_KEY;
  const secretKey = __ENV.ALPACA_SECRET_KEY;

  if (!baseUrl || !apiKey || !secretKey) {
    throw new Error(
      'Required Alpaca environment variables are missing.'
    );
  }

  const response = http.get(
    `${baseUrl}/v2/account`,
    {
      headers: {
        'APCA-API-KEY-ID': apiKey,
        'APCA-API-SECRET-KEY': secretKey,
        'Accept': 'application/json',
      },
    }
  );

  check(response, {
    'status is 200': (r) => r.status === 200,
    'response body is not empty': (r) =>
      r.body !== null && r.body.length > 0,
  });

  sleep(1);
}