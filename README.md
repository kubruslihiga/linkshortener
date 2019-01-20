Documentation
=============

Instructions:
------------------
1. Package application
    * ./mvnw clean install
1. Configure a MongoDB and Springboot application:
    * docker-compose up --build
1. (Optional) MongoDB must be running, execute: 
    * ./mvnw spring-boot:run -pl shortener-services
    
Services available:
-------------------
1. POST - /post
   * Body content:
      - "urlComplete" : "website link"
1. GET - /{link_id}
   * If link_id exists in the database, the application redirects to website posted.
1. GET - /link/access
   * This services returns all links ordered by counter access. Request parameters:
      - "counterSort" -> "ASC or DESC"
