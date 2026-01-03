#!/bin/bash
# Quick test run with summary - UTF-8 enabled

export LANG=en_US.UTF-8
export LC_ALL=en_US.UTF-8
export JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8 -Dconsole.encoding=UTF-8"

echo "==============================================="
echo "Running OrangeHRM Tests (49 test cases)"
echo "==============================================="

mvn clean test -Dbrowser=chrome -Dremote=true -Dheadless=true 2>&1 | tee test-output.log

echo ""
echo "==============================================="
echo "Test Summary"
echo "==============================================="
grep -E "Tests run:|Running:" test-output.log | tail -20
echo ""
echo "Failed tests:"
grep "FAILURE!" test-output.log | head -10
echo ""
echo "Full log saved to: test-output.log"
