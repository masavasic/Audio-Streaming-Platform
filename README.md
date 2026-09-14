# Audio Streaming Platform

Java-based client-server audio platform developed as an academic project.

The system is organized around a **central REST server**, multiple **JMS-connected subsystems**, a **client application**, and **MySQL persistence**.

The platform supports user management, audio content, subscriptions, ratings, favorites, and listening history.

## Project Overview

The system follows a distributed architecture consisting of:

- Client application
- Central server
- Subsystem 1
- Subsystem 2
- Subsystem 3
- MySQL databases
- JMS-based communication
- REST endpoints
- UML models

The central server acts as a gateway between the client and backend subsystems.

Backend responsibilities are divided between multiple subsystems, while communication between components is performed through JMS.

## Main Features

The platform supports functionality related to:

- User management
- User locations
- Audio categories
- Audio uploads
- Audio content management
- Subscription packages
- User subscriptions
- Ratings
- Favorite audio recordings
- Listening history
- Communication between distributed services
- REST-based client access
- Persistent storage in MySQL

## Architecture

A simplified architecture of the system is:

```text
                Client Application
                        |
                        v
                  REST Requests
                        |
                        v
                 Central Server
                        |
                        v
                       JMS
                /       |       \
               v        v        v
         Subsystem 1 Subsystem 2 Subsystem 3
               |        |        |
               v        v        v
                 MySQL Databases
```

The central server exposes the system functionality to the client through REST endpoints.

The subsystems communicate through Java Message Service and are responsible for different groups of domain operations.

## Central Server

The `CentralniServer` project represents the central REST gateway.

It contains:

- REST configuration
- REST endpoints
- JPA entities
- Persistence configuration
- Web application configuration

The server receives requests from the client and coordinates communication with backend subsystems.

## Client Application

The `KlijentskaAplikacija` project contains the client-side component.

Its role is to:

- Send requests to the central server
- Invoke available platform operations
- Display returned results
- Provide access to the system functionality

## Subsystem 1

`Podsistem1` contains functionality related to entities such as:

- `Korisnik`
- `Mesto`

This subsystem handles a part of the user-related domain logic.

## Subsystem 2

`Podsistem2` contains entities such as:

- `Audiosnimak`
- `Kategorija`
- `Korisnik`
- `Mesto`
- `Pripada`

This subsystem is responsible for audio content and category-related functionality.

## Subsystem 3

`Podsistem3` contains entities such as:

- `Audiosnimak`
- `Korisnik`
- `Mesto`
- `Ocena`
- `Omiljenisnimak`
- `Paket`
- `Pretplata`
- `Slusanje`

This part of the system manages additional platform functionality such as subscriptions, ratings, favorites, and listening activity.

## Domain Model

The project includes entities representing the main concepts of the audio platform.

### User-related entities

```text
Korisnik
Mesto
```

### Audio-related entities

```text
Audiosnimak
Kategorija
Pripada
```

### Subscription-related entities

```text
Paket
Pretplata
```

### User interaction entities

```text
Ocena
Omiljenisnimak
Slusanje
```

These entities represent relationships between users, audio content, subscription packages and platform activity.

## REST API

The central server exposes functionality through REST services.

The REST layer acts as the external interface of the system.

Conceptually:

```text
Client
   |
   v
HTTP / REST
   |
   v
Central Server
   |
   v
JMS
   |
   v
Subsystems
```

This separation allows the client to communicate through HTTP while internal service communication remains asynchronous and message-based.

## JMS Communication

The backend architecture uses **Java Message Service (JMS)** for communication between the central server and subsystems.

JMS provides message-based communication between distributed application components.

The architecture separates:

- External communication through REST
- Internal communication through JMS

This approach allows responsibilities to be distributed across several subsystems.

## Persistence

The project uses **JPA** for persistence and **MySQL** as the relational database system.

Persistence configuration is defined through `persistence.xml` files inside the project modules.

The project contains database scripts inside:

```text
baze/
```

These scripts can be used to create or initialize the required database structures.

## UML

The repository also includes UML project files:

```text
UML/
```

These models describe parts of the system design and domain structure.

They were created as part of the design phase before or alongside implementation.

## Repository Structure

```text
Audio-Streaming-Platform/
│
├── CentralniServer/
│   ├── pom.xml
│   └── src/
│
├── KlijentskaAplikacija/
│   ├── pom.xml
│   └── src/
│
├── Podsistem1/
│   ├── build.xml
│   ├── nbproject/
│   └── src/
│
├── Podsistem2/
│   ├── build.xml
│   ├── nbproject/
│   └── src/
│
├── Podsistem3/
│   ├── build.xml
│   ├── nbproject/
│   └── src/
│
├── UML/
├── baze/
├── centralni.txt
├── kategorije za is1 projekat.txt
├── persistec.txt
├── uputstvo za pedosisteme.txt
├── .gitignore
└── README.md
```

## Technologies

The project uses:

- Java
- Java EE / Jakarta EE concepts
- REST
- JAX-RS
- JMS
- JPA
- MySQL
- Maven
- Ant
- NetBeans
- UML
- SQL

## Key Concepts Demonstrated

The project demonstrates:

- Client-server architecture
- Distributed systems
- Service decomposition
- REST API design
- Message-based communication
- JMS
- Relational persistence
- JPA entity modeling
- MySQL databases
- Separation of responsibilities between services
- Domain modeling
- UML-based system design

## Building the Project

Different modules use different build systems.

### Maven modules

Projects such as the central server and client application contain:

```text
pom.xml
```

and can be built with Maven.

Example:

```bash
mvn clean package
```

### Ant / NetBeans modules

The subsystem projects contain:

```text
build.xml
```

and can be built using Ant or directly through NetBeans.

Example:

```bash
ant
```

## Database Setup

Database scripts are located inside:

```text
baze/
```

The required MySQL databases should be created before starting the application.

The corresponding connection settings can then be configured in the module `persistence.xml` files.

## Running the System

A typical startup flow is:

```text
1. Start MySQL
2. Create/import the required databases
3. Configure persistence settings
4. Start the application server / JMS infrastructure
5. Start backend subsystems
6. Start the central server
7. Run the client application
```

The exact configuration depends on the local Java EE / application server environment.

## Academic Project

This repository contains an academic project focused on:

- Information systems
- Distributed Java applications
- REST services
- JMS communication
- Database persistence
- Client-server architecture