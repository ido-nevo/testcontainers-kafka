Feature: Basic calculator operations

Background:
  Given Kafka producer and consumer are initialized

Scenario Outline: Add two numbers and check the result
  Given Calculator service is initialized
  When number <first> is added with number <second> and published to a kafka topic
  Then Kafka consumes the result as <result>
  Examples:
    | first | second | result |
    | 11    | 11     | 22     |
    | 4     | 13     | 17     |
    | 8     | -13    | -5     |
    | 1     | 0      | 1      |
    | 9     | 11     | 20     |
