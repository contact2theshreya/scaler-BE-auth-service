# DB query
create database userservice;
create user userservice;
grant all privileges on userservice.* to userservice;

1) once u add spring security dependency in pom ,by defaut it will make all api endpoint authenticated.
2) so add springsecurity configuration class to override this functionality
3) ![img.png](img.png)
4) use apache common lang library to generate random string for toekn
5) ![img_1.png](img_1.png)
6) ![img_2.png](img_2.png)
# Test with jwt
![img_3.png](img_3.png)
![img_4.png](img_4.png)![img_5.png](img_5.png)
# OAUTH
As oauth is standard so many of client lke postman will be supporting tha standard
https://docs.spring.io/spring-authorization-server/reference/getting-started.html
Add security config file and copy code from above here u will get all info about oidc
postman config
![img_14.png](img_14.png)
![img_15.png](img_15.png)
scope-openid profile
click on get new access token
![img_16.png](img_16.png)

give permission afetr login
![img_9.png](img_9.png) proceed and now toen is ready
![img_10.png](img_10.png)
make custom user details and disable in memory db storage for oayth
before using goggle authorization server -sign in via google first u have to register
your client
currently registerdclient factoruy in code is in memory but google will store in it DB 
the information of its client
https://docs.spring.io/spring-authorization-server/reference/guides/how-to-custom-claims-authorities.html
https://docs.spring.io/spring-authorization-server/reference/guides/how-to-jpa.html

## Note
in mysql every row has size of 16KB thaat is 65,536bytes else jpa will give error
so one solution to use @Lob as text column in model coz this doesn't count as size of the row
Run insertclient test once and then comment out and pust same details in postman and run
tell spring securith it is safe to convert customuserdetails class in jwt-security restriction
so use @jsondeserialize-safe to deserialize to json

once postman generate token
complete jwt is stored in token as well as client information who logins and authorized this
login with contact2theshreya@gmail.com/sherpauts-user table
![img_11.png](img_11.png)
![img_12.png](img_12.png)
decode jwt token
![img_13.png](img_13.png)
![img_17.png](img_17.png)
![img_18.png](img_18.png)

