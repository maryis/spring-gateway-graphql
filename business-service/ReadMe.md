
#It is a project as the middle layer. //this project not used any more 
It features:
  - GraphQL server for FrontEnd (done for Rotation)
  - GraphQL client for Backend (done for Rotation)
  - Authentication (done based on http-basic and JWT)
 
Later, it can feature:
  - Access Control
  - Load Balancing
  - Request Routing
  - protocol translation
  - Custom API
  - Caching
------------------------------
**Note:** 

two interesting things that worth noticing during design phase:

1- when the backend is GraphQL server, using apollo-link-state instead of Redux is recommended:
https://www.robinwieruch.de/react-apollo-link-state-tutorial/
although there are ways to have both of them in the React project.

2- common web security mechanisms that rely on headers, does not work for GraphQL servers!
Authorization in business layer is a must! Also we need a mechanism for preventing DOS, that can be timeout
https://carvesystems.com/news/the-5-most-common-graphql-security-vulnerabilities/

-----------------------tech stack------------------------------

Graphql server is based on "com.graphql-java-kickstart:graphql-spring-boot-starter". 
by using this dependency, there is no need to write controller or datafetcher. steps:
- we define our schema in resources/graphql (configure path in properties file)
- we define GraphQLMutationResolver and GraphQLQueryResolver classes and methods for
each query/mutation/subscription operation in schema files. These classes should be annotated as Service.
- there is no need to write resolver for complex types such as courses.
- graphql-spring automatically map each "localhost:9090/graphql" request to its resolver based on the query-name 
in url's body
- Also it provides a "../graphiql" endpoint for testing the server
- my resolvers return fake data .
- a useful like: https://graphql-code-generator.com/
- APIs:    
  - API urls:  localhost:9090/graphql
  - testAPI urls: localhost:9090/graphiql
  - authenticate api: localhost:9090/authenticate
-----------------------------------------
Logging framework is based on logback. spring-boot default logging framework: logback, so in initial it looks 
for logback** config file and if it does not find anything, it loads defaults.
if I want to switch to log4j2 framework, I should:

1- add dep

2- change name of config file

3- change content of config file a little

4- no change in class files

-----------------------------------------
I used JWT as authorization mechanism, and authentication is provided by following endpoine:
../authenticate, and the default user/pass are in config-file. 
a config for basic Auth is also provided

-----------------------
graphql-client part is based on restTemplate

.....

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphQLRequest {

    private final String query;
    private final String operationName;
    private final Map<String, Object> variables;

    @JsonCreator
    public GraphQLRequest(@JsonProperty("query") String query,
                          @JsonProperty("operationName") String operationName,
                          @JsonProperty("variables") Map<String, Object> variables) {
        this.query = query;
        this.operationName = operationName;
        this.variables = variables != null ? variables : Collections.emptyMap();
    }

    public String getQuery() {
        return query;
    }

    public String getOperationName() {
        return operationName;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}

/////////
@CrossOrigin
@PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@ResponseBody
public Map<String, Object> graphql(@RequestBody GraphQLRequest graphQLRequest, HttpServletRequest httpRequest) {
    // em context estamos passando o Request, usamos para fazer as verificacoes de autenticacao com GraphQl 
    ExecutionInput.Builder inputBuilder = ExecutionInput.newExecutionInput()
                .query(graphQLRequest.getQuery())
                .operationName(graphQLRequest.getOperationName())
                .variables(graphQLRequest.getVariables()) //this is the line you were missing
                .context(httpRequest);
    return executionResult.toSpecification();
}
/////////////////
POST request #
A standard GraphQL POST request should use the application/json content type, and include a JSON-encoded body of the following form:
request:
{
  "query": "...",
  "operationName": "...",
  "variables": { "myVariable": "someValue", ... }
  
}
response:
{
  "data": { ... },
  "errors": [ ... ]
}
If there were no errors returned,  the "errors" field should not be present on the response. 
 If no data returned, according to the GraphQL spec, 
 the "data" field should only be included if the error occurred during execution.