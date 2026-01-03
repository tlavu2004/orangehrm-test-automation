# PowerShell script to run tests with UTF-8 encoding
# Usage: .\run-tests-utf8.ps1

# Set console to UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$env:JAVA_TOOL_OPTIONS = "-Dfile.encoding=UTF-8"

# Run Maven tests
mvn clean test -Dbrowser=chrome -Dremote=true -Dheadless=true
