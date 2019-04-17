### Users

### Restaurants
#### get All Restaurants
`curl -s http://localhost:8080/voting/rest/restaurants`

#### get Restaurants 100003
`curl -s http://localhost:8080/voting/rest/restaurants/100003`

#### get Restaurants not found
`curl -s -v http://localhost:8080/voting/rest/restaurants/100548`

#### delete Restaurants
`curl -s -X DELETE http://localhost:8080/voting/rest/restaurants/100002 --user admin@gmail.com:admin`

#### create Restaurants
`curl -s -X POST -d '{"name":"Created restaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/restaurants --user admin@gmail.com:admin`

#### update Restaurants
`curl -s -X PUT -d '{"name":"Updated restaurant"}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/restaurants/100003 --user admin@gmail.com:admin`

### Dishes
#### get All Dishes
`curl -s "http://localhost:8080/voting/rest/dishes?restaurantId=100003&date=2019-01-01"`

#### get Dishes 100010
`curl -s http://localhost:8080/voting/rest/dishes/100010?restaurantId=100003`

#### get Dishes not found
`curl -s -v http://localhost:8080/voting/rest/dishes/100852?restaurantId=100003`

#### delete Dishes
`curl -s -X DELETE http://localhost:8080/voting/rest/dishes/100011?restaurantId=100003 --user admin@gmail.com:admin`

#### create Dishes
`curl -s -X POST -d '{"name":"Created dish","price":300,"date":"2019-03-15"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/dishes?restaurantId=100003 --user admin@gmail.com:admin`

#### update Dishes
`curl -s -X PUT -d '{"name":"Updated dish","price":300,"date":"2019-04-01"}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/dishes/100008?restaurantId=100003 --user admin@gmail.com:admin`

### Votes
#### get All Results
`curl -s "http://localhost:8080/voting/rest/votes?date=2019-02-01"`

#### get Votes 100015
`curl -s http://localhost:8080/voting/rest/votes/100015 --user admin@gmail.com:admin`

#### get Votes not found
`curl -s -v http://localhost:8080/voting/rest/votes/100428`

// Не понятно на т.м. как написать запросы для create и update 
#### create Votes
`curl -s -X POST -d '{"name":"Created dish","price":300,"date":"2019-03-15"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/votes`

#### update Votes
`curl -s -X PUT -d '{"name":"Updated dish","price":300,"date":"2019-04-01"}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/votes/100015`