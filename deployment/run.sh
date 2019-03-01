#!/bin/bash

/usr/bin/docker run --rm -p 127.0.0.1:8080:8080 --name tron-monitor -v /opt/docker/tron-monitor:/config fundrequestio/tron-node-monitor:master