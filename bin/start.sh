#!/bin/sh

export GH_BASIC_CLIENT_ID=01189998819991197253
export GH_BASIC_SECRET_ID=01189998819991197253

mvn exec:java -Dexec.mainClass="xorrr.github.io.Main"
