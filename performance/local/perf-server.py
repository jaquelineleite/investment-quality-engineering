from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
import json
import time

class Handler(BaseHTTPRequestHandler):

    def do_GET(self):
        if self.path == "/health":

            payload = {
                "status": "UP",
                "service": "investment-qe-performance-lab"
            }

            body = json.dumps(payload).encode("utf-8")

            self.send_response(200)
            self.send_header(
                "Content-Type",
                "application/json"
            )
            self.send_header(
                "Content-Length",
                str(len(body))
            )
            self.end_headers()

            self.wfile.write(body)

        else:
            self.send_response(404)
            self.end_headers()

    def log_message(self, format, *args):
        return


if __name__ == "__main__":

    server = ThreadingHTTPServer(
        ("localhost", 8080),
        Handler
    )

    print(
        "Performance lab running at "
        "http://localhost:8080"
    )

    server.serve_forever()