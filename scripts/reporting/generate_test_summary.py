import json
import xml.etree.ElementTree as ET
from pathlib import Path


ROOT = Path(".")
REPORT_DIR = ROOT / "reports" / "observability"

REPORT_DIR.mkdir(
    parents=True,
    exist_ok=True
)


def collect_reports():

    reports = []

    for xml_file in ROOT.glob(
        "**/target/surefire-reports/TEST-*.xml"
    ):

        tree = ET.parse(xml_file)
        root = tree.getroot()

        suite = {
            "file": str(xml_file),
            "name": root.attrib.get(
                "name",
                xml_file.stem
            ),
            "tests": int(
                root.attrib.get("tests", 0)
            ),
            "failures": int(
                root.attrib.get("failures", 0)
            ),
            "errors": int(
                root.attrib.get("errors", 0)
            ),
            "skipped": int(
                root.attrib.get("skipped", 0)
            ),
            "time": float(
                root.attrib.get("time", 0.0)
            ),
            "testcases": []
        }

        for testcase in root.findall(
            ".//testcase"
        ):

            suite["testcases"].append({
                "class": testcase.attrib.get(
                    "classname",
                    ""
                ),
                "name": testcase.attrib.get(
                    "name",
                    ""
                ),
                "time": float(
                    testcase.attrib.get(
                        "time",
                        0.0
                    )
                )
            })

        reports.append(suite)

    return reports


def main():

    suites = collect_reports()

    total_tests = sum(
        s["tests"]
        for s in suites
    )

    failures = sum(
        s["failures"]
        for s in suites
    )

    errors = sum(
        s["errors"]
        for s in suites
    )

    skipped = sum(
        s["skipped"]
        for s in suites
    )

    duration = sum(
        s["time"]
        for s in suites
    )

    passed = (
        total_tests
        - failures
        - errors
        - skipped
    )

    pass_rate = (
        passed / total_tests * 100
        if total_tests
        else 0
    )

    all_tests = []

    for suite in suites:
        all_tests.extend(
            suite["testcases"]
        )

    slowest = sorted(
        all_tests,
        key=lambda item: item["time"],
        reverse=True
    )[:10]

    summary = {
        "total": total_tests,
        "passed": passed,
        "failures": failures,
        "errors": errors,
        "skipped": skipped,
        "passRate": round(
            pass_rate,
            2
        ),
        "durationSeconds": round(
            duration,
            3
        ),
        "suites": suites,
        "slowestTests": slowest
    }

    json_path = (
        REPORT_DIR
        / "test-summary.json"
    )

    json_path.write_text(
        json.dumps(
            summary,
            indent=2
        ),
        encoding="utf-8"
    )

    md = []

    md.append(
        "# Test Observability Summary"
    )

    md.append("")
    md.append(
        f"- Total tests: {total_tests}"
    )
    md.append(
        f"- Passed: {passed}"
    )
    md.append(
        f"- Failures: {failures}"
    )
    md.append(
        f"- Errors: {errors}"
    )
    md.append(
        f"- Skipped: {skipped}"
    )
    md.append(
        f"- Pass rate: {pass_rate:.2f}%"
    )
    md.append(
        f"- Total test time: {duration:.3f}s"
    )

    md.append("")
    md.append(
        "## Suites"
    )
    md.append("")

    md.append(
        "| Suite | Tests | Failures | Errors | Time (s) |"
    )

    md.append(
        "|---|---:|---:|---:|---:|"
    )

    for suite in suites:

        md.append(
            f"| {suite['name']} "
            f"| {suite['tests']} "
            f"| {suite['failures']} "
            f"| {suite['errors']} "
            f"| {suite['time']:.3f} |"
        )

    md.append("")
    md.append(
        "## Slowest tests"
    )
    md.append("")

    md.append(
        "| Test | Time (s) |"
    )

    md.append(
        "|---|---:|"
    )

    for test in slowest:

        md.append(
            f"| {test['class']}.{test['name']} "
            f"| {test['time']:.3f} |"
        )

    markdown_path = (
        REPORT_DIR
        / "test-summary.md"
    )

    markdown_path.write_text(
        "\n".join(md),
        encoding="utf-8"
    )

    print()
    print(
        "TEST OBSERVABILITY REPORT"
    )
    print("=" * 60)

    print(
        f"Total tests: {total_tests}"
    )

    print(
        f"Passed: {passed}"
    )

    print(
        f"Failures: {failures}"
    )

    print(
        f"Errors: {errors}"
    )

    print(
        f"Pass rate: {pass_rate:.2f}%"
    )

    print(
        f"Duration: {duration:.3f}s"
    )

    print()
    print(
        f"Generated: {markdown_path}"
    )

    print(
        f"Generated: {json_path}"
    )


if __name__ == "__main__":
    main()