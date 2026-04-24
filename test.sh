#!/usr/local/bin/bash

curl -X POST http://localhost:8080/adapter/ping \
  -H "Content-Type: application/json" \
  -d '{"hello":"world"}'

curl http://localhost:8080/admin/routes | jq 

