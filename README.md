# Camunda demo project

The project demonstrates working with:
- [Camunda Platform](https://github.com/camunda/camunda-bpm-platform)
- [Zeebe](https://github.com/camunda-cloud/zeebe)

Slides: 
- [Camunda pitfalls](https://noti.st/akonyaev/DjOaCs/camunda-pitfalls) - about working with Camunda Platform

## Project content
The demo application includes:
- process schemas:
    - bpmn/ - for Camunda Platform
    - zeebe/ - for Zeebe
- Camunda engine (Camunda Platform part)
- service task delegates - will be called by Camunda Platform
- service task workers - will be called by Zeebe
- rest-api controllers:
    - ProcessController - for running processes in Camunda Platform
    - ProcessControllerV2 - for running processes in Zeebe

Zeebe docker-containers located in the folder `./zeebe-local` includes:
- zeebe - standalone broker
- elasticsearch - needed for zeebe to export data via zeebe-hazelcast-exporter
- monitor - tool for monitor deployments, active processes, incidents etc
- operate - camunda official tool for monitoring processes, but it is without schema deployment option
- tasklist - camunda official tool for executing user-tasks

## Building and running
```shell
./gradlew clean build
```

```shell
./gradlew bootRun
```
or just run the CamundaDemoApplication class in IntelliJ IDEA.

To work with the Camunda platform, you just need to run the application.
To work with Zeebe, you also need to run zeebe-containers before the application:
```shell
cd ./zeebe-local
./up.sh
```

When the application starts up, it automatically deploys bpmn-schemas to Camunda Platform and Zeebe.

# UI
- Demo application [REST API](http://localhost:8080/swagger-ui.html#) for starting processes
- [Camunda Platform applications](http://localhost:8080) - Cockpit, Tasklist and Admin tools
- Zeebe applications:
  - [Monitor](http://localhost:8082)  
  - [Operate](http://localhost:8083)
  - [Tasklist](http://localhost:8084)

To login Camunda and Zeebe applications use `demo/demo`.
