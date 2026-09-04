📌 Overview
aop_practice_demo is a Java/Spring AOP practice project demonstrating:

Aspect‑Oriented Programming (AOP)

Logging with SLF4J

Field‑level encryption using AES/CBC

Validation using custom utility classes

JDBC persistence without ORM

Spring’s IoC container and component scanning

Multi‑aspect interception of setter methods

Decoupling via interfaces and repositories

This project simulates a contact form submission workflow where user input is:

Validated

Encrypted

Persisted to MySQL

Assigned a database‑generated ID

Logged and decrypted for demonstration

All of this is triggered automatically through Spring AOP advice.

🧩 Project Architecture
Core Bean
ContactFormBean  
A Spring component representing a contact form submission. Fields include:

firstName

lastName

telephone

email

message

preferred contact method

auto‑generated MySQL ID

AOP Aspects
The project uses multiple @Around advices to intercept setter calls:

Logging of setter execution

Validation of first and last name

Encryption of all string fields

Automatic persistence once all fields are set

Retrieval of bean ID

Decryption for demonstration output

Repositories
PublishFormMessages — JDBC insert into beans table

EncryptedStorage — stores AES key + IV for each bean ID

BeanUtilities — retrieves bean ID from MySQL

Utilities
CryptoUtils — AES key generation, IV generation, encryption/decryption

ValidateContactFormUtil — ASCII‑based validation rules

🔐 Encryption
The project uses:

AES

CBC mode

PKCS5Padding

Random IV per bean

Base64 encoding for storage

Keys and IVs are stored in a separate secret_keys table.

🗄️ Database Schema (MySQL)
beans
Column	Type	Notes
id	INT	AUTO_INCREMENT
first_name	VARCHAR	Encrypted
last_name	VARCHAR	Encrypted
email	VARCHAR	Encrypted
telephone	VARCHAR	Encrypted
message	TEXT	Encrypted
method	VARCHAR	Enum label


secret_keys
Column	Type	Notes
id	INT	FK → beans.id
secret_key	VARBINARY	AES key bytes
iv_index	VARBINARY	IV bytes


⚙️ Spring Configuration
The project uses:

java
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {
    "logging",
    "net.beans.www",
    "validation",
    "bean.repository"
})
This enables:

Automatic discovery of components

Automatic weaving of AOP aspects

Dependency injection for repositories and beans

🚀 How It Works (Execution Flow)
User sets fields on ContactFormBean

AOP intercepts each setter

Validation runs

Encryption runs

Once all fields are set, the bean is persisted

Bean ID is retrieved

Encryption keys are stored

Decrypted values are logged for demonstration

🧪 Purpose
This project is a hands‑on demonstration of:

Spring AOP fundamentals

Multi‑aspect method interception

Secure data handling

JDBC integration

Logging and debugging

Architectural experimentation

It is not intended for production use, but as a learning tool for mastering Spring’s AOP capabilities and encryption workflows.

📚 Technologies Used
Java 17

Spring Framework

Spring AOP

SLF4J / Logback

MySQL

JDBC

AES/CBC Encryption

👤 Author
Joseph Ridener  
Aerospace + IT dual‑track student
Java backend developer in training
Spring Boot, Maven, AOP, and encryption enthusiast
