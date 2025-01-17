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