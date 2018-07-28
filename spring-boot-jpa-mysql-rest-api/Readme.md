# Spring Boot, MySQL, JPA, Hibernate Rest API Tutorial

Build Restful CRUD API for a simple Note-Taking application using Spring Boot, Mysql, JPA and Hibernate.

## Requirements

1. Java - 1.8.x

2. Maven - 3.x.x

3. Mysql - 5.x.x

## Steps to Setup


**1. Create Mysql database**
```bash
create database notes_app
```

**2. Change mysql username and password as per your installation**

+ open `src/main/resources/application.properties`

+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**3. Build and run the app using maven**

```bash
mvn package
java -jar target/easy-notes-1.0.0.jar
```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

## Explore Rest APIs

使用Postman测试：
设置Header:Content-Type:application/json

    GET /api/notes
    
    POST /api/notes
           测试：raw:{"title":"1 title","content":"1 content"}
    	
    GET /api/notes/{noteId}
            测试：/api/notes/1
    
    PUT /api/notes/{noteId}
            测试：/api/notes/1
           要修改的内容 raw:{"title":"1 title","content":"1 content"}        
    
    DELETE /api/notes/{noteId}
            测试：/api/notes/1

