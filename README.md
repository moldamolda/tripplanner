
## Her kommer min dokumentation til eksamensprojekt 2024
## helt nederst findes respons på http-test




## 3.3.1
jeg har ikke added 	/trips/populate, men i stedet get("/guide/{guideId}", tripController::getTripsByGuide, Role.ANYONE);

## 3.3.5 Theoretical question: Why do we suggest a PUT method for adding a guide to a trip instead of a POST method? Write the answer in your README.md file.

put bruges normalt til opdatere eksisterende ressurser
I denne sammenhæng betragtes det at tilføje en guide til en eksisterende tur som en opdatering af turressourcen, 
hvor guidens tilknytning ændres.

den er idempotent, hvilket betyder at hvis man kalder 
put med samme data flere gange, vil resultatet være det samme



## 8.3 Adding security roles to the endpoints will make the corresponding Rest Assured Test fail. Now the request will return a 401 Unauthorized response. D
escribe how you would fix the failing tests in your README.md file, or if time permits, implement the solution so your tests pass.
først populate databasen med testbrugere i forksllige roller som admin og user.
jeg ville sætte en header på hver request .header("Authorization", "Bearer " + token), for så kan vi authenticate med token




## 3.3.2 Test the endpoints using a dev.http file. Document the output in your README.md file to verify the functionality.

GET /trips
Accept: application/json

[
{
"id": 1,
"startTime": [
2024,
5,
10,
8,
30
],
"endTime": [
2024,
5,
10,
12,
30
],
"startPosition": "Default Start Position",
"name": "Central Park Tour",
"price": 50.0,
"category": "CITY",
"guideId": 1
},
{
"id": 2,
"startTime": [
2024,
6,
15,
9,
0
],
"endTime": [
2024,
6,
15,
13,
0
],
"startPosition": "Default Start Position",
"name": "Beach Adventure",
"price": 80.0,
"category": "BEACH",
"guideId": 2
}
]
Status Code: 200 OK
Time Taken: 971 ms

GET /trips/1
Accept: application/json
{
"id": 1,
"startTime": [
2024,
5,
10,
8,
30
],
"endTime": [
2024,
5,
10,
12,
30
],
"startPosition": "Default Start Position",
"name": "Central Park Tour",
"price": 50.0,
"category": "CITY",
"guideId": 1
}
Status Code: 200 OK

POST /trips
Content-Type: application/json

{
"startTime": "2024-05-10T08:30:00",
"endTime": "2024-05-10T12:30:00",
"startPosition": "Central Park",
"name": "City thing",
"price": 100.0,
"category": "CITY",
"guideId": 1
}
{
"id": 3,
"startTime": [
2024,
5,
10,
8,
30
],
"endTime": [
2024,
5,
10,
12,
30
],
"startPosition": "Central Park",
"name": "City thing",
"price": 100.0,
"category": "CITY",
"guideId": 1
}
Status Code: 201 Created

PUT /trips/1
Content-Type: application/json

{
"startTime": "2024-05-12T09:00:00",
"endTime": "2024-05-12T13:00:00",
"startPosition": "Updated Park",
"name": "Updated City Tour",
"price": 120.0,
"category": "CITY",
"guideId": 1
}
{
"id": 1,
"startTime": [
2024,
5,
12,
9,
0
],
"endTime": [
2024,
5,
12,
13,
0
],
"startPosition": "Updated Park",
"name": "Updated City Tour",
"price": 120.0,
"category": "CITY",
"guideId": 1
}
Status Code: 200 OK

DELETE /trips/2
Status Code: 204 No Content


PUT /trips/1/guides/1
200 OK

GET /trips/guide/1
Accept: application/json
[
{
"id": 1,
"startTime": [
2024,
5,
12,
9,
0
],
"endTime": [
2024,
5,
12,
13,
0
],
"startPosition": "Updated Park",
"name": "Updated City Tour",
"price": 120.0,
"category": "CITY",
"guideId": 1
},
{
"id": 3,
"startTime": [
2024,
5,
10,
8,
30
],
"endTime": [
2024,
5,
10,
12,
30
],
"startPosition": "Central Park",
"name": "City thing",
"price": 100.0,
"category": "CITY",
"guideId": 1
}
]
Status Code: 200 OK

