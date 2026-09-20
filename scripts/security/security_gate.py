import re
import subprocess
import sys
from pathlib import Path


def tracked_files():
    result = subprocess.run(
        ["git", "ls-files", "-z"],
        capture_output=True,
        check=True
    )

    return [
        Path(item.decode("utf-8"))
        for item in result.stdout.split(b"\0")
        if item
    ]


def is_allowed_placeholder(value):
    normalized = (
        value.strip()
        .strip('"')
        .strip("'")
        .lower()
    )

    if not normalized:
        return True

    allowed_fragments = (
        "${{ secrets.",
        "${",
        "<",
        ">",
        "your_",
        "example",
        "placeholder",
        "replace",
        "change_me",
        "changeme",
        "dummy"
    )

    return any(
        fragment in normalized
        for fragment in allowed_fragments
    )


def main():

    failures = []

    files = tracked_files()

    # --------------------------------------------------------
    # Files that must never be committed
    # --------------------------------------------------------

    for path in files:

        normalized = path.as_posix().lower()

        if normalized.endswith("/.env") or normalized == ".env":
            failures.append(
                f"Tracked secret file detected: {path}"
            )

        if normalized.endswith(".pem"):
            failures.append(
                f"Tracked PEM file detected: {path}"
            )

        if normalized.endswith(".p12"):
            failures.append(
                f"Tracked PKCS12 file detected: {path}"
            )


    # --------------------------------------------------------
    # Secret patterns
    # --------------------------------------------------------

    secret_patterns = {
        "AWS access key":
            re.compile(
                r"AKIA[0-9A-Z]{16}"
            ),

        "GitHub token":
            re.compile(
                r"gh[pousr]_[A-Za-z0-9_]{20,}"
            ),

        "Private key":
            re.compile(
                r"-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----"
            ),

        "Generic sk token":
            re.compile(
                r"sk-[A-Za-z0-9]{20,}"
            ),
    }


    alpaca_variables = (
        "ALPACA_API_KEY",
        "ALPACA_SECRET_KEY",
        "APCA_API_KEY_ID",
        "APCA_API_SECRET_KEY"
    )


    for path in files:

        try:
            text = path.read_text(
                encoding="utf-8"
            )
        except (
            UnicodeDecodeError,
            OSError
        ):
            continue


        # ----------------------------------------------------
        # Known token patterns
        # ----------------------------------------------------

        for name, pattern in secret_patterns.items():

            if pattern.search(text):
                failures.append(
                    f"{name} detected in tracked file: {path}"
                )


        # ----------------------------------------------------
        # Alpaca secrets must come from env/secrets
        # ----------------------------------------------------

        for line_number, line in enumerate(
            text.splitlines(),
            start=1
        ):

            for variable in alpaca_variables:

                pattern = re.compile(
                    rf"^\s*{variable}\s*[:=]\s*(.+?)\s*$"
                )

                match = pattern.match(line)

                if not match:
                    continue

                value = match.group(1)

                if not is_allowed_placeholder(value):

                    failures.append(
                        f"Hardcoded {variable} detected "
                        f"in {path}:{line_number}"
                    )


    if failures:

        print()
        print("SECURITY GATE: FAILED")
        print("=" * 60)

        for failure in failures:
            print(f"[FAIL] {failure}")

        print()
        sys.exit(1)


    print()
    print("SECURITY GATE: PASSED")
    print("=" * 60)
    print(
        f"Tracked files scanned: {len(files)}"
    )
    print(
        "No tracked .env, private keys, "
        "known tokens or hardcoded Alpaca secrets detected."
    )


if __name__ == "__main__":
    main()