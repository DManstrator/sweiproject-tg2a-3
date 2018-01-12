[status]: https://travis-ci.org/sweIhm/sweiproject-tg2a-3.svg?branch=master
[heroku]: https://img.shields.io/badge/Heroku-muasicaly-purple.svg?colorB=6762a6
[FAQ]: https://img.shields.io/badge/Wiki-FAQ-blue.svg
[license]: https://img.shields.io/github/license/sweIhm/sweiproject-tg2a-3.svg
[ ![status][] ](https://travis-ci.org/sweIhm/sweiproject-tg2a-3/)
[ ![heroku] ](https://muasicaly.herokuapp.com/)
[ ![FAQ] ](https://github.com/sweIhm/sweiproject-tg2a-3/wiki/About-MUAS−i−Caly)
[ ![license][] ](https://github.com/sweIhm/sweiproject-tg2a-3/tree/master/LICENSE)

# MUAS-i-Caly
Checkout our [fancy Website](https://sweihm.github.io/sweiproject-tg2a-3/)!

This guide walks you through the process of creating a "hello world" [RESTful web service](/understanding/REST) with Spring.

## What you'll build

You'll build a service that will accept HTTP GET requests at:

```
http://localhost:8080/greeting
```

and respond with a [JSON](/understanding/JSON) representation of a greeting:

```json
{"id":1,"content":"Hello, World!"}
```

You can customize the greeting with an optional `name` parameter in the query string:

```
http://localhost:8080/greeting?name=User
```

The `name` parameter value overrides the default value of "World" and is reflected in the response:

```json
{"id":1,"content":"Hello, User!"}
```


## What you'll need

https://raw.githubusercontent.com/spring-guides/getting-started-macros/master/prereq_editor_jdk_buildtools.adoc

https://raw.githubusercontent.com/spring-guides/getting-started-macros/master/how_to_complete_this_guide.adoc

https://raw.githubusercontent.com/spring-guides/getting-started-macros/master/hide-show-gradle.adoc

https://raw.githubusercontent.com/spring-guides/getting-started-macros/master/hide-show-maven.adoc

https://raw.githubusercontent.com/spring-guides/getting-started-macros/master/hide-show-sts.adoc


## Create a resource representation class

Now that you've set up the project and build system, you can create your web service.

Begin the process by thinking about service interactions.

The service will handle `GET` requests for `/greeting`, optionally with a `name` parameter in the query string. The `GET` request should return a `200 OK` response with JSON in the body that represents a greeting. It should look something like this:

```json
{
    "id": 1,
    "content": "Hello, World!"
}
```

The `id` field is a unique identifier for the greeting, and `content` is the textual representation of the greeting.

To model the greeting representation, you create a resource representation class. Provide a plain old java object with fields, constructors, and accessors for the `id` and `content` data:

`src/main/java/hello/Greeting.java`
```java
link:complete/src/main/java/hello/Greeting.java[]
```
| | |
|-|-|
Note|As you see in steps below, Spring uses the [Jackson JSON](http://wiki.fasterxml.com/JacksonHome) library to automatically marshal instances of type `Greeting` into JSON.

Next you create the resource controller that will serve these greetings.


## Create a resource controller

In Spring's approach to building RESTful web services, HTTP requests are handled by a controller. These components are easily identified by the [`@RestController`](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestController.html) annotation, and the `GreetingController` below handles `GET` requests for `/greeting` by returning a new instance of the `Greeting` class:

`src/main/java/hello/GreetingController.java`
```java
link:complete/src/main/java/hello/GreetingController.java[]
```

This controller is concise and simple, but there's plenty going on under the hood. Let's break it down step by step.

The `@RequestMapping` annotation ensures that HTTP requests to `/greeting` are mapped to the `greeting()` method.

| | |
|-|-|
Note|The above example does not specify `GET` vs. `PUT`, `POST`, and so forth, because `@RequestMapping` maps all HTTP operations by default. Use `@RequestMapping(method=GET)` to narrow this mapping.

`@RequestParam` binds the value of the query string parameter `name` into the `name` parameter of the `greeting()` method. This query string parameter is explicitly marked as optional (`required=true` by default): if it is absent in the request, the `defaultValue` of "World" is used.

The implementation of the method body creates and returns a new `Greeting` object with `id` and `content` attributes based on the next value from the `counter`, and formats the given `name` by using the greeting `template`.

A key difference between a traditional MVC controller and the RESTful web service controller above is the way that the HTTP response body is created. Rather than relying on a [view technology](/understanding/view-templates) to perform server-side rendering of the greeting data to HTML, this RESTful web service controller simply populates and returns a `Greeting` object. The object data will be written directly to the HTTP response as JSON.

This code uses Spring 4's new [`@RestController`](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestController.html) annotation, which marks the class as a controller where every method returns a domain object instead of a view. It's shorthand for `@Controller` and `@ResponseBody` rolled together.

The `Greeting` object must be converted to JSON. Thanks to Spring's HTTP message converter support, you don't need to do this conversion manually. Because [Jackson 2](http://wiki.fasterxml.com/JacksonHome) is on the classpath, Spring's [`MappingJackson2HttpMessageConverter`](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/http/converter/json/MappingJackson2HttpMessageConverter.html) is automatically chosen to convert the `Greeting` instance to JSON.


## Make the application executable

Although it is possible to package this service as a traditional [WAR](/understanding/WAR) file for deployment to an external application server, the simpler approach demonstrated below creates a standalone application. You package everything in a single, executable JAR file, driven by a good old Java `main()` method. Along the way, you use Spring's support for embedding the [Tomcat](/understanding/Tomcat) servlet container as the HTTP runtime, instead of deploying to an external instance.


`src/main/java/hello/Application.java`
```java
link:complete/src/main/java/hello/Application.java[]
```

https://raw.githubusercontent.com/spring-guides/getting-started-macros/master/spring-boot-application.adoc

https://raw.githubusercontent.com/spring-guides/getting-started-macros/master/build_an_executable_jar_subhead.adoc

https://raw.githubusercontent.com/spring-guides/getting-started-macros/master/build_an_executable_jar_with_both.adoc

Logging output is displayed. The service should be up and running within a few seconds.


## Test the service

Now that the service is up, visit http://localhost:8080/greeting, where you see:

```json
{"id":1,"content":"Hello, World!"}
```

Provide a `name` query string parameter with http://localhost:8080/greeting?name=User. Notice how the value of the `content` attribute changes from "Hello, World!" to "Hello User!":

```json
{"id":2,"content":"Hello, User!"}
```

This change demonstrates that the `@RequestParam` arrangement in `GreetingController` is working as expected. The `name` parameter has been given a default value of "World", but can always be explicitly overridden through the query string.

Notice also how the `id` attribute has changed from `1` to `2`. This proves that you are working against the same `GreetingController` instance across multiple requests, and that its `counter` field is being incremented on each call as expected.


## Summary

Congratulations! You've just developed a RESTful web service with Spring.

## See Also

The following guides may also be helpful:

- [Accessing GemFire Data with REST](https://spring.io/guides/gs/accessing-gemfire-data-rest/)
- [Accessing MongoDB Data with REST](https://spring.io/guides/gs/accessing-mongodb-data-rest/)
- [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
- [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
- [Accessing Neo4j Data with REST](https://spring.io/guides/gs/accessing-neo4j-data-rest/)
- [Consuming a RESTful Web Service](https://spring.io/guides/gs/consuming-rest/)
- [Consuming a RESTful Web Service with AngularJS](https://spring.io/guides/gs/consuming-rest-angularjs/)
- [Consuming a RESTful Web Service with jQuery](https://spring.io/guides/gs/consuming-rest-jquery/)
- [Consuming a RESTful Web Service with rest.js](https://spring.io/guides/gs/consuming-rest-restjs/)
- [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
- [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
- [React.js and Spring Data REST](https://spring.io/guides/tutorials/react-and-spring-data-rest/)
- [Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot/)
- [Creating API Documentation with Restdocs](https://spring.io/guides/gs/testing-restdocs/)
- [Enabling Cross Origin Requests for a RESTful Web Service](https://spring.io/guides/gs/rest-service-cors/)
- [Building a Hypermedia-Driven RESTful Web Service](https://spring.io/guides/gs/rest-hateoas/)
- [Circuit Breaker](https://spring.io/guides/gs/circuit-breaker/)

https://raw.githubusercontent.com/spring-guides/getting-started-macros/master/footer.adoc
