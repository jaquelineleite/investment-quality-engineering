import http from 'k6/http';
import { check, sleep } from 'k6';
import { validatePerformanceTarget } from '../helpers/performance-guard.js';

export const options = {
  stages: [
    { duration: '10s', target: 5 },
    { duration: '5s', target: 30 },
    { duration: '20s', target: 30 },
    { duration: '10s', target: 0 },
  ],

  thresholds: {
    http_req_failed: ['rate<0.05'],
    http_req_duration: ['p(95)<2000'],
  },
};

const baseUrl = __ENV.PERF_BASE_URL;

validatePerformanceTarget(baseUrl);

export default function () {

  const response = http.get(`${baseUrl}/health`);

  check(response, {
    'status is 200': (r) => r.status === 200,
  });

  sleep(1);
}