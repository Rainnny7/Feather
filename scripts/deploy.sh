#!/bin/bash

# Switch dir
cd "$(dirname "$0")/.." || exit

# Deploy
mvn deploy -Pgen-javadocs -B -Dstyle.color=always --update-snapshots -T12 -e