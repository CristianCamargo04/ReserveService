# ReserveService

*ReserveService* es una API REST desarrollada con Spring Boot y Java 17, diseñada para realizar un servicio de gestión de reservas de vuelos , utilizando broker de mensajeria Kafka, almacenamiento en PostgreSQL y JWT con Spring Security para la Autentificación.

La API cuenta con una interfaz gráfica para la exploración y prueba de endpoints, accesible en:  
[http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

---

## Requisitos previos

Para garantizar el correcto funcionamiento del proyecto, es necesario contar con las siguientes herramientas instaladas:
- *Java 21:* Requerido para ejecutar la aplicación.
- *Maven 3.8 o superior:* Para gestionar las dependencias y realizar la compilación.
- *Docker y Docker Compose (opcional):* Para gestionar los servicios de PostgreSQL y Kafka mediante contenedores.

---

## Instalación y ejecución

1. *Clonar el repositorio:*  
   Ejecute los siguientes comandos para obtener el código fuente:
   ```bash  
      git clone https://github.com/CristianCamargo04/mathchallenger-app.git  
      cd mathchallenger
   ```

2. *Levantamiento de servicios:*  
   Los contenedores necesarios para la ejecución del proyecto se definen en el archivo docker-compose.yml. 
   Para iniciar los servicios, ejecute:
   ```bash  
      docker-compose up -d
   ```

3. *Acceso a Swagger:*  
   Una vez que la aplicación esté en ejecución, acceda a la interfaz Swagger en:  
   [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

---

## Tecnologías utilizadas

El proyecto está construido con las siguientes tecnologías:
- *Spring Boot:* Framework para el desarrollo de aplicaciones web con Java.
- *Maven:* Herramienta de gestión de dependencias.
- *PostgreSQL:* Base de datos relacional para el almacenamiento persistente.
- *Kafka:* Broker de mensajeria para la ingesta y el procesamiento de datos de streaming en tiempo real.
---  