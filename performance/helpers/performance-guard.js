export function validatePerformanceTarget(baseUrl) {
  if (!baseUrl) {
    throw new Error(
      'PERF_BASE_URL is required for load, stress and spike tests.'
    );
  }

  if (baseUrl.includes('paper-api.alpaca.markets')) {
    throw new Error(
      'Heavy performance tests are blocked against Alpaca Paper Trading.'
    );
  }

  if (baseUrl.includes('api.alpaca.markets')) {
    throw new Error(
      'Heavy performance tests are blocked against Alpaca.'
    );
  }
}