# Product Information Management
This microservice has product information. Consume AddedProductBySupplier and UpdatedProductBySupplier topic :
* Created product 
* Updated product
* Produce UserNotificationMessage topic 


## Tech Stack
* Java JDK 11
* Maven 3.8.1
* Spring Boot v2.9.2
* Kafka 2.8.0
* Postgresql 13
* Swagger

## Running the Application
#### Build the app
`mvn clean install`

### Download and Install Kafka
https://kafka.apache.org/downloads

https://www.youtube.com/watch?v=EUzH9khPYgs

### Run Kafka
`.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties`

`.\bin\windows\kafka-server-start.bat .\config\server.properties`

Create Test Topic:
`.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic TestTopic`

### Up PostgreSQL DB
`docker pull postgres`

`docker run --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=pass123 -d postgres`

## Application Details

Once application is up, api documentation can be seen at `http://localhost:8081/swagger-ui.html`

### Sample Endpoint

#### [POST] /products/ (updateProduct)

```
Body: 
{
  "productId": 1,
  "barcode": "A123B456C",
  "categoryId": 1,
  "imageURL": "product1.jpg",
  "price": 100.0,
  "stock": 500,
  "title": "Mavi Gomlek"
}
```

