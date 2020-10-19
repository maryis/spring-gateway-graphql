## this application is a Gateway + Authentication Server
## I think we should use compass-services as Authentication Server later

#to run:

\#gradlew timetable-gateway:bootRun


# to do :
- exception handling 
- downstream exception - 500


#good to know:
1)
Zuul is a blocking API. A blocking gateway api makes use of as many threads as the number of incoming requests
spring cloud gateway is a non-blocking gateway. functionalities:
- routing
- web-filters (rate limit, size limit , change..)
- security : separating backend from outside 
- metric gathering

2)
Gateway Metrics Filter
To enable Gateway Metrics add spring-boot-starter-actuator as a project dependency. 
Then, by default, the Gateway Metrics Filter runs as long as the property spring.cloud.gateway.metrics.enabled is not set to false. 
This filter adds a timer metric named "gateway.requests" with the following tags:
- routeId: The route id
- routeUri: The URI that the API will be routed to
- outcome: Outcome as classified by HttpStatus.Series
- status: Http Status of the request returned to the client
These metrics are then available to be scraped from /actuator/metrics/gateway.requests



