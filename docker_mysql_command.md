## MySQL
- `docker run -it -d --name mysql-container -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password --restart always -v mysql_data_container:/var/lib/mysql mysql:latest`
---

## Adminer(MySQL Client tools)
- `docker run -it -d --name adminer -p 8080:8080 -e ADMINER_DEFAULT_SERVER=mysql-container --restart always adminer:latest`
---