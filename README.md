Documentation
=============

Build application:
------------------
1. Install Java 8
1. Install docker
    1. Run docker daemon for compilation
1. Install MySQL
    1. Configure root password to 'root'
    1. Create database livecozydb and livecozydbtest (this one for integration tests)
1. Install maven
    1. Execute maven in root project folder: mvn clean install
    1. If you want skip test -Dmaven.test.skip

Configure docker:
-----------------
1. Permit user execute docker command
    * sudo groupadd docker
    * sudo usermod -aG docker $USER
    * Logout log back
    * Check if user may execute docker without sudo

Configure application:
----------------------
1. Open data-mysql.sql inside and execute all commands in your database

Run application:
----------------
* Run application directly eclipse
    * Run main class LiveCozyApplication
* Run by maven
    * Execute mvn spring-boot:run inside folder livecozy-services
* Run by docker
    * After build, execute docker run -it net=host springio/livecozy-services-app -p 8080:8080