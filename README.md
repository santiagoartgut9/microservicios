https://img.shields.io/badge/Java-21-orange
https://img.shields.io/badge/Spring%2520Boot-3.3.2-brightgreen
https://img.shields.io/badge/AWS-Lambda-orange
https://img.shields.io/badge/API-Gateway-yellow
https://img.shields.io/badge/Auth-Cognito-blue
https://img.shields.io/badge/DB-DynamoDB-green
https://img.shields.io/badge/Deployment-Serverless-Fiolet
https://img.shields.io/badge/License-MIT-blue

# 🚀 StreamApp - Microservicios con AWS Lambda y Serverless
Este proyecto implementa una aplicación de red social tipo "Stream" utilizando una arquitectura de microservicios desplegada en AWS con Serverless Framework. La aplicación permite a los usuarios registrarse, iniciar sesión y publicar mensajes cortos (hasta 140 caracteres).

La arquitectura está compuesta por microservicios independientes para autenticación, gestión de usuarios y publicaciones, utilizando AWS Cognito para la autenticación, DynamoDB para la persistencia y API Gateway para exponer los endpoints.

# 📋 Tabla de contenidos
Descripción

Estructura del proyecto

Arquitectura

Flujo de datos

Endpoints principales

Seguridad

Prerrequisitos

Despliegue

Comandos de verificación

Licencia y autor

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
Configurar DynamoDB
La tabla posts se crea automáticamente durante el despliegue del Post Service.


3💻 Comandos útiles
Gestión de usuarios Cognito
```
bash
# Listar usuarios
aws cognito-idp list-users --user-pool-id us-east-1_xxxxxxxxx

# Crear usuario manualmente
aws cognito-idp sign-up \
    --client-id xxxxxxxxxxxxxxxxxxxxxxxxxx \
    --username "usuario@ejemplo.com" \
    --password "Password123!" \
    --user-attributes Name=email,Value=usuario@ejemplo.com Name=name,Value="Nombre Usuario"

# Confirmar usuario
aws cognito-idp admin-confirm-sign-up \
    --user-pool-id us-east-1_xxxxxxxxx \
    --username "usuario@ejemplo.com"
```
Verificar despliegue
bash
# Ver logs en tiempo real
serverless logs -f auth -t

# Listar funciones desplegadas
serverless list functions

# Información del despliegue
serverless info

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
🔧 Desarrollo local
Ejecutar con Serverless Offline
bash
# En cada servicio
serverless offline

# O con modo watch
serverless offline start
Variables de entorno para desarrollo
bash
# Crear archivo .env en cada servicio
COGNITO_USER_POOL_ID=us-east-1_xxxxxxxxx
COGNITO_CLIENT_ID=xxxxxxxxxxxxxxxxxxxxxxxxxx
POSTS_TABLE=streamapp-posts-dev
Estructura de un post en DynamoDB
json
{
  "id": "1234567890",
  "contenido": "Este es mi primer post en StreamApp!",
  "autor": "usuario@ejemplo.com",
  "createdAt": "2024-01-01T12:00:00.000Z",
  "updatedAt": "2024-01-01T12:00:00.000Z"
}
🎯 Próximas características
Likes y reacciones a posts

Sistema de seguidores

Notificaciones en tiempo real

Búsqueda de posts y usuarios

Edición y eliminación de posts

Subida de imágenes

Modo oscuro

📄 Licencia
Este proyecto está bajo la Licencia MIT. Ver el archivo LICENSE para más detalles.

👨‍💻 Autor
Agustin Santiago
📧 agsantiago030102@outlook.com
🔗 GitHub Profile
