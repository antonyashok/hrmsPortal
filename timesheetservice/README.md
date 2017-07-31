# Timetrack Developer Environment Setup

## Local Environment Setup

## 1. Environmental Dependencies
    This procedure has been successfully completed on Mac, Windows and Linux

### Prerequisite
    The Following software must be installed.
    
    1. Java 8
    2. MySql + MySql Workbench
    3. Maven 3.2.5
    4. Elasticsearch 2.4.0
    5. Git (Version control)
    6. Docker
    
## 2. Get the source code from single repository
      * Create a root folder (<your favorite location>/techmango) 
      * Use the following command to clone 'timesheetservice' project
        
        git clone http://128.199.223.27/techmango/timesheetservice.git 
      
## 3. Database setup

### Timetrack MySql Database

#### Installation Instructions
      * Install MySql 
      * launch the mysql database server
      * Install MySql Workbench
      * Launch MySql Workbench
      * Test that the default root credential works
         
#### Install schema and user configurations
      1. Open workbench and login as 'root' user.
      2. Click 'Users and Privileges' to create new timetrack user.
         username = tmuser and password = tmpass
      3. Give all permission to newly created user tmuser.
      4. Create new schema named 'timetrack'.
      5. Try to login using newly created user (tmuser).
      6. Import the existing database.
      
## 4. Build Timetrack service docker image
      * Goto the project timesheetservice and execute the below command to create docker image
      
        $ mvn package docker:build

## 5. Running Timetrack Service with Docker
      
        $ docker run -e "SPRING_PROFILES_ACTIVE=dev" -t <image name>
       
## 6. Build Timetrackui docker image
      * Goto the project timetrackui and execute the below command to create docker image
      
        $ mvn package docker:build

## 7. Running Timetrackui with Docker
      
        $ docker run -e "SPRING_PROFILES_ACTIVE=dev" -t <image name>
        
## 8. Running Timetrack Service Manually
      * Goto the project timesheetservice and execute the below command to create jar
         
        $ mvn clean package      
      * Now we can execute this jar by using below commands.
          
          java -jar timesheetservice-1.0.0-SNAPSHOT.jar

## 9. Running Timetrack UI Manually
      * Goto the project timetrackui and execute the below command to create jar
         
        $ mvn clean package      
      * Now we can execute this jar by using below commands.
          
          java -jar timetrackui-1.0.0-SNAPSHOT.jar
    
## Gatling Load Test

### 1. Running Gatling Load Test in Configuration Service Manually
		* Please make sure the below things,
			
			1. Check the gatling service URL and authentication configuration in config file.
			2. Please ensure the timetrackservice is running.
			  
		* Goto the project timetrackservice and execute the below command to test
         
       	  $ mvn gatling:execute -DgatlingConfigUrl=http://configuration_service:8888/TimesheetManagement-profile_name.properties
