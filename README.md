
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.2-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-Enabled-success)
![JWT](https://img.shields.io/badge/Auth-JWT-green)
![BCrypt](https://img.shields.io/badge/Encryption-BCrypt-lightgrey)
![REST API](https://img.shields.io/badge/API-REST-blue)
![Maven](https://img.shields.io/badge/Build-Maven-red)
![H2 Database](https://img.shields.io/badge/Database-H2-lightblue)
![HTML5](https://img.shields.io/badge/Frontend-HTML5-red)
![CSS3](https://img.shields.io/badge/Style-CSS3-blue)
![JavaScript](https://img.shields.io/badge/Frontend-JavaScript-yellow)
![License](https://img.shields.io/badge/License-MIT-blue)
![Node.js](https://img.shields.io/badge/Node.js-18.x-green)
![AWS Lambda](https://img.shields.io/badge/AWS-Lambda-orange)
![Serverless Framework](https://img.shields.io/badge/Framework-Serverless-red)
![Amazon API Gateway](https://img.shields.io/badge/AWS-API%20Gateway-yellow)
![Amazon Cognito](https://img.shields.io/badge/Auth-AWS%20Cognito-blueviolet)
![Amazon DynamoDB](https://img.shields.io/badge/Database-DynamoDB-blue)
![Amazon S3](https://img.shields.io/badge/Storage-Amazon%20S3-lightblue)




# 🚀 StreamApp - Microservicios con AWS Lambda y Serverless
Este proyecto implementa una aplicación de red social tipo "Stream" utilizando una arquitectura de microservicios desplegada en AWS con Serverless Framework. La aplicación permite a los usuarios registrarse, iniciar sesión y publicar mensajes cortos (hasta 140 caracteres).

La arquitectura está compuesta por microservicios independientes para autenticación, gestión de usuarios y publicaciones, utilizando AWS Cognito para la autenticación, DynamoDB para la persistencia y API Gateway para exponer los endpoints.

## 📋 Tabla de Contenidos

1. [Descripción](#-descripción)
2. [Estructura del proyecto](#-estructura-del-proyecto-microservicios)
3. [Arquitectura](#-arquitectura)
4. [Flujo de datos](#-flujo-de-datos)
5. [Endpoints principales](#-endpoints-principales)
6. [Seguridad](#-seguridad)
7. [Prerrequisitos](#-prerrequisitos)
8. [Despliegue](#-despliegue)
9. [Comandos de verificación](#-comandos-de-verificación)
10. [Licencia y autor](#-licencia-y-autor)

# Video
https://youtu.be/MVKW_-U8FgM

# 📝 Descripción
StreamApp es una aplicación web que permite a los usuarios publicar y ver mensajes cortos (similar a Twitter). La aplicación está construida con una arquitectura de microservicios, donde cada servicio (auth, user, post) es independiente y se despliega como una función AWS Lambda.

Características principales:

Registro y autenticación de usuarios con AWS Cognito.

Publicación de mensajes cortos (hasta 140 caracteres).

Feed de publicaciones en tiempo real.

Interfaz web responsive.

Despliegue automatizado con Serverless Framework.

Tecnologías utilizadas:

Frontend: HTML5, CSS3, JavaScript (ES6+)

Backend: Node.js 18.x (AWS Lambda)

Autenticación: AWS Cognito

Base de datos: Amazon DynamoDB

API: Amazon API Gateway

Infraestructura como código: Serverless Framework

Almacenamiento estático: Amazon S3

# 📂 Estructura del proyecto microservicios:

```text
stream-monolith/
└── stream-microservices/
    ├── auth-service/                 # Servicio de autenticación
    │   ├── handler.js               # Lambda function
    │   ├── serverless.yml           # Configuración Serverless
    │   └── package.json             # Dependencias Node.js
    ├── user-service/                 # Servicio de usuarios
    │   ├── handler.js               # Lambda function
    │   ├── serverless.yml           # Configuración Serverless
    │   └── package.json             # Dependencias Node.js
    ├── post-service/                 # Servicio de publicaciones
    │   ├── handler.js               # Lambda function
    │   ├── serverless.yml           # Configuración Serverless
    │   └── package.json             # Dependencias Node.js
    └── src/main/resources/static/   # Frontend estático
        ├── index.html               # Aplicación principal
        ├── app.js                   # Lógica del frontend
        ├── styles.css               # Estilos CSS
        └── assets/                  # Recursos estáticos


```


# 🔁 Flujo de datos
Registro de Usuario:
```
text
Frontend → API Gateway → Auth Service → Cognito User Pool
```
Autenticación:
```
text
Frontend → API Gateway → Auth Service → Cognito → JWT Tokens
```
Publicar Post:
```
text
Frontend → API Gateway → Post Service → DynamoDB
```
Obtener Feed:
```
text
Frontend → API Gateway → Post Service → DynamoDB → Frontend
```
Perfil de Usuario:
```
text
Frontend → API Gateway → User Service → Cognito → Frontend
```
# 🧪 Endpoints principales
Servicio de Autenticación
```
POST /auth/register - Registrar nuevo usuario

POST /auth/login - Iniciar sesión
```
# 👤 Servicio de Usuarios
```
GET /users/me - Obtener información del usuario actual
```
# 📝 Servicio de Publicaciones
```
GET /posts - Obtener todos los posts (ordenados por fecha)

POST /posts - Crear nuevo post
```
# ☁️ Despliegue
Prerrequisitos
bash
```
# Instalar Serverless Framework
npm install -g serverless

# Configurar AWS CLI
aws configure

# Variables de entorno requeridas
export COGNITO_USER_POOL_ID=us-east-1_xxxxxxxxx
export COGNITO_CLIENT_ID=xxxxxxxxxxxxxxxxxxxxxxxxxx
export POSTS_TABLE=streamapp-posts-table
```
Desplegar servicios
```
bash
# Desplegar Auth Service
cd auth-service
serverless deploy

# Desplegar User Service  
cd ../user-service
serverless deploy

# Desplegar Post Service
cd ../post-service
serverless deploy

# Desplegar Frontend
aws s3 sync src/main/resources/static/ s3://tu-bucket-s3/ --acl public-read
```
⚙️ Configuración
Configurar Cognito User Pool
```
bash

# Crear User Pool
aws cognito-idp create-user-pool \
    --pool-name StreamAppUsers \
    --username-attributes email \
    --auto-verified-attributes email

# Crear App Client
aws cognito-idp create-user-pool-client \
    --user-pool-id $USER_POOL_ID \
    --client-name StreamAppWebClient \
    --explicit-auth-flows "ALLOW_USER_PASSWORD_AUTH" "ALLOW_REFRESH_TOKEN_AUTH"
```
pruebas fotos:

<img width="1330" height="710" alt="image" src="https://github.com/user-attachments/assets/ad165e30-18ac-4c3e-b8f1-deec7dcac135" />

<img width="1298" height="617" alt="image" src="https://github.com/user-attachments/assets/a1917c7e-1f22-4d0d-bc3a-9edcde9a3776" />

<img width="894" height="190" alt="image" src="https://github.com/user-attachments/assets/a7e3aadd-1f65-4ece-9cf0-dc1280322f6a" />

<img width="792" height="203" alt="image" src="https://github.com/user-attachments/assets/b50df028-9955-44bc-9051-1d034209feb2" />

<img width="767" height="194" alt="image" src="https://github.com/user-attachments/assets/bc61df67-c07b-4c57-b477-9f3b6e0e2ef9" />

<img width="501" height="135" alt="image" src="https://github.com/user-attachments/assets/234eb63f-ff55-4b29-84db-71a4babf20bc" />


📂 Estructura del proyecto local 
```
.
│   .mvn/
│   pom.xml
│
├───src
│   ├───main
│   │   ├───java
│   │   │   └───com
│   │   │       └───arteaga
│   │   │           └───stream
│   │   │               │   StreamMonolithApplication.java
│   │   │               │
│   │   │               ├───config
│   │   │               │       DataLoader.java
│   │   │               │       GlobalExceptionHandler.java
│   │   │               │       SecurityConfig.java
│   │   │               │
│   │   │               ├───controller
│   │   │               │       AuthController.java
│   │   │               │       HiloController.java
│   │   │               │       PostController.java
│   │   │               │       UsuarioController.java
│   │   │               │
│   │   │               ├───dto
│   │   │               │       CreateHiloRequest.java
│   │   │               │       CreatePostRequest.java
│   │   │               │       CreateUserRequest.java
│   │   │               │       PostResponse.java
│   │   │               │
│   │   │               ├───model
│   │   │               │       Hilo.java
│   │   │               │       Post.java
│   │   │               │       Usuario.java
│   │   │               │
│   │   │               ├───repository
│   │   │               │       HiloRepository.java
│   │   │               │       PostRepository.java
│   │   │               │       UsuarioRepository.java
│   │   │               │
│   │   │               ├───security
│   │   │               │       CustomUserDetailsService.java
│   │   │               │       JwtFilter.java
│   │   │               │       JwtUtil.java
│   │   │               │
│   │   │               └───service
│   │   │                       HiloService.java
│   │   │                       PostService.java
│   │   │                       UsuarioService.java
│   │   │
│   │   └───resources
│   │       │   application.properties
│   │       │
│   │       ├───static
│   │       │       404.html
│   │       │       app.js
│   │       │       index.html
│   │       │       styles.css
│   │       │
│   │       └───templates/
│   │
│   └───test
│           (posibles pruebas unitarias)
│
└───target/
        (archivos generados al compilar)
```
🧩 Descripción del desarrollo

Para este proyecto se diseñó e implementó una API REST utilizando Spring Boot, que permite a los usuarios registrarse, autenticarse y publicar mensajes cortos (de hasta 140 caracteres), simulando el comportamiento de una red social tipo Twitter.

El sistema se desarrolló bajo una arquitectura monolítica estructurada en capas, con las siguientes entidades principales:

Usuario: gestiona la información y credenciales de los usuarios.

Hilo (Stream): representa el flujo general donde se agrupan los posts.

Post: almacena los mensajes creados por los usuarios.

Se implementaron controladores, servicios y repositorios para manejar la lógica de negocio, junto con seguridad basada en JWT para la autenticación. Además, se desarrolló una interfaz web con HTML, CSS y JavaScript para consumir el API y permitir la interacción con los usuarios desde el navegador.

En resumen, el proyecto consolida un monolito funcional y seguro que expone servicios REST para la gestión de usuarios y publicaciones, junto con un frontend ligero para visualizar y crear posts en tiempo real.

# pruebas fotos:

<img width="598" height="271" alt="image" src="https://github.com/user-attachments/assets/8cfc5ab5-19af-46fb-8bca-571bd97e5276" />
<img width="595" height="292" alt="image" src="https://github.com/user-attachments/assets/1220c11b-80cd-4a46-833b-45245a05b5d4" />
<img width="596" height="308" alt="image" src="https://github.com/user-attachments/assets/5b6fae41-b48a-41df-b43d-b1b67ff7888c" />
<img width="581" height="396" alt="image" src="https://github.com/user-attachments/assets/afde5dd6-56b1-4bda-99fe-e726e8c32f56" />
<img width="591" height="390" alt="image" src="https://github.com/user-attachments/assets/13548341-7f9e-4c83-9e97-96d149abe34f" />
<img width="603" height="228" alt="image" src="https://github.com/user-attachments/assets/2355b125-dbcc-4834-bdb2-7feeed1a2fc1" />
<img width="588" height="321" alt="image" src="https://github.com/user-attachments/assets/95d5ea10-a669-4ca8-9a6a-3c493fa7d8ae" />
<img width="613" height="290" alt="image" src="https://github.com/user-attachments/assets/07efdfea-c51a-4a6c-a116-fb5db9b0a519" />






👨‍💻 Autor
Agustin Santiago
📧 agsantiago030102@outlook.com
🔗 GitHub Profile
