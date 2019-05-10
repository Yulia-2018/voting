### Admin Users
#### get All Users
`curl -s http://localhost:8080/voting/rest/admin/users --user admin@gmail.com:admin`

#### get Users 100000
`curl -s http://localhost:8080/voting/rest/admin/users/100000 --user admin@gmail.com:admin`

#### get by email Users 100000
`curl -s http://localhost:8080/voting/rest/admin/users/by?email=user@yandex.ru --user admin@gmail.com:admin`

#### get Users not found
`curl -s http://localhost:8080/voting/rest/admin/users/1 --user admin@gmail.com:admin`
`curl -s http://localhost:8080/voting/rest/admin/users/by?email=user123@yandex.ru --user admin@gmail.com:admin`

#### create Users
`curl -s -X POST -d '{"name":"NewUser","email":"NewUser@yandex.ru","password":"NewPassword","roles":["ROLE_USER"]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/admin/users --user admin@gmail.com:admin`

#### update Users
`curl -s -X PUT -d '{"id":100000,"name":"Updated User","email":"user@yandex.ru","password":"password","roles":["ROLE_USER"]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/admin/users/100000 --user admin@gmail.com:admin`

### Profile Users
#### get
`curl -s http://localhost:8080/voting/rest/profile --user user@yandex.ru:password`

#### register
`curl -s -X POST -d '{"name":"Registered User","email":"RegisteredUser@yandex.ru","password":"159358"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/profile/register`

#### update
`curl -s -X PUT -d '{"id":100000,"name":"User","email":"user@yandex.ru","password":"password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/profile  --user user@yandex.ru:password`

### Restaurants
#### get All Restaurants
`curl -s http://localhost:8080/voting/rest/restaurants --user user@yandex.ru:password`

#### get All Restaurants With Dishes
`curl -s http://localhost:8080/voting/rest/restaurants/withDishes?date=2019-01-01 --user user@yandex.ru:password`

#### get Restaurants 100003
`curl -s http://localhost:8080/voting/rest/restaurants/100003 --user user@yandex.ru:password`

#### get Restaurants not found
`curl -s http://localhost:8080/voting/rest/restaurants/100548 --user user@yandex.ru:password`

#### create Restaurants
`curl -s -X POST -d '{"name":"Created restaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/restaurants --user admin@gmail.com:admin`

#### update Restaurants
`curl -s -X PUT -d '{"name":"Updated restaurant"}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/restaurants/100003 --user admin@gmail.com:admin`

### Dishes
#### get All Dishes
`curl -s "http://localhost:8080/voting/rest/dishes?restaurantId=100003&date=2019-01-01" --user user@yandex.ru:password`

#### get Dishes 100010
`curl -s http://localhost:8080/voting/rest/dishes/100010?restaurantId=100003 --user user@yandex.ru:password`

#### get Dishes not found
`curl -s http://localhost:8080/voting/rest/dishes/100004?restaurantId=100003 --user user@yandex.ru:password`

#### create Dishes
`curl -s -X POST -d '{"name":"Created dish","price":300}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/dishes?restaurantId=100003 --user admin@gmail.com:admin`

#### update Dishes
`curl -s -X PUT -d '{"name":"Updated dish","price":300}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/dishes/100012?restaurantId=100002 --user admin@gmail.com:admin`

#### delete Dishes
`curl -s -X DELETE http://localhost:8080/voting/rest/dishes/100012?restaurantId=100002 --user admin@gmail.com:admin`

### Votes
#### get All Results Voting
`curl -s "http://localhost:8080/voting/rest/votes?date=2019-02-01" --user user@yandex.ru:password`

#### get Votes 100016
`curl -s http://localhost:8080/voting/rest/votes/100016 --user admin@gmail.com:admin`

#### get Votes not found
`curl -s http://localhost:8080/voting/rest/votes/100016 --user user@yandex.ru:password`

#### create Votes
`curl -s -X POST -d '{}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/votes?restaurantId=100003 --user admin@gmail.com:admin`

#### update Votes
`curl -s -X PUT -d '{}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/votes/100017?restaurantId=100002 --user user@yandex.ru:password`

#### validate with Error
`curl -s -X PUT -d '{}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/dishes/100008?restaurantId=100003 --user admin@gmail.com:admin`
`curl -s -X PUT -d '{"date":"2019-03-01"}' -H 'Content-Type: application/json' http://localhost:8080/voting/rest/votes/100017?restaurantId=100002 --user user@yandex.ru:password`