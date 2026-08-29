#!/bin/bash

echo "=========================================="
echo "FINAL BUSINESS FLOW TEST"
echo "=========================================="

echo -e "\n1. Recording Donation: A+ 10 units"
curl -X POST http://localhost:8080/api/donations \
  -H "Content-Type: application/json" \
  -d '{"bloodGroup": "A+", "units": 10}'

echo -e "\n\n2. Stock Summary"
curl -X GET http://localhost:8080/api/stock

echo -e "\n\n3. Creating Request: A+ 6 units (should succeed)"
curl -X POST http://localhost:8080/api/requests \
  -H "Content-Type: application/json" \
  -d '{"requesterName": "John Doe", "bloodGroup": "A+", "requestedUnits": 6}'

echo -e "\n\n4. Fulfilling Request (should be FULFILLED)"
curl -X POST http://localhost:8080/api/requests/1/fulfill

echo -e "\n\n5. Stock after fulfillment (should be 4)"
curl -X GET http://localhost:8080/api/stock

echo -e "\n\n6. Creating Request: A+ 8 units (should be REJECTED)"
curl -X POST http://localhost:8080/api/requests \
  -H "Content-Type: application/json" \
  -d '{"requesterName": "Jane Smith", "bloodGroup": "A+", "requestedUnits": 8}'

echo -e "\n\n7. Fulfilling Request (should be REJECTED)"
curl -X POST http://localhost:8080/api/requests/2/fulfill

echo -e "\n\n8. Final Stock (should still be 4 - UNCHANGED!)"
curl -X GET http://localhost:8080/api/stock

echo -e "\n\n9. Trying to fulfill Request 1 again (should be 409 CONFLICT)"
curl -X POST http://localhost:8080/api/requests/1/fulfill

echo -e "\n\n=========================================="
echo "TEST COMPLETE!"
echo "=========================================="