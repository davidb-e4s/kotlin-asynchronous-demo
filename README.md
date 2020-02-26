# Kotlin Asynchronous Service Demo
A sample project to show how to use Kotlin/Coroutines with Spring boot to create asynchronous services.
This project uses the latest Spring Data milestone (at time of writing) which has added coroutine support for
MongoDB and does away with Mono/Flux making it easy to write asynchronous services in a synchronous style

# Tests
Run the tests to see the examples in action. I have also shown how to run Spring MVC tests and JUnit tests on a 
codebase using coroutines. 

# MongoDB
You will need a local instance of MongoDB installed in order to run the example. The connection string is located
in application.properties 
