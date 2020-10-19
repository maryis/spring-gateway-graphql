Zuul is a blocking API. A blocking gateway api makes use of as many threads as the number of incoming requests
spring cloud gateway is a non-blocking gateway. functionalities:
- routing
- web-filters (rate limit, size limit , change..)
- security : separating backend from outside 
- metric gathering

## this application is a Gateway + Authentication Server
## I think we should have compass-services as Authentication Server later

