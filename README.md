
# Solution
* [Stack](#stack)
* [Test](#run-test)
* [Tech and design concerns](#tech-and-design-concerns)


## Stack
 * kotlin 1.3
 * spring-boot 2
 * junit 5
 * test-containers
 
## Run test

The test task run two test with 10 and 300 messages call  
 
```bash
 ./gradlew test
```

## Tech and design concerns

The application emulates concurrency thanks to kafka. 
When no employee is free to answer a call, the message is retained in the topic waiting for a kafka consumer.

The amount of consumers is manged by 
```yaml 
spring:
 kafka:
  listener:
   concurrency: 10
```
When running test its creates kafka docker container managed by test-container 
