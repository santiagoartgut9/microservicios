https://img.shields.io/badge/Java-21-orange
https://img.shields.io/badge/Spring%2520Boot-3.3.2-brightgreen
https://img.shields.io/badge/AWS-Lambda-orange
https://img.shields.io/badge/API-Gateway-yellow
https://img.shields.io/badge/Auth-Cognito-blue
https://img.shields.io/badge/DB-DynamoDB-green
https://img.shields.io/badge/Deployment-Serverless-Fiolet
https://img.shields.io/badge/License-MIT-blue

🚀 StreamApp - Microservicios con AWS Lambda y Serverless
Este proyecto implementa una aplicación de red social tipo "Stream" utilizando una arquitectura de microservicios desplegada en AWS con Serverless Framework. La aplicación permite a los usuarios registrarse, iniciar sesión y publicar mensajes cortos (hasta 140 caracteres).

La arquitectura está compuesta por microservicios independientes para autenticación, gestión de usuarios y publicaciones, utilizando AWS Cognito para la autenticación, DynamoDB para la persistencia y API Gateway para exponer los endpoints.

📋 Tabla de contenidos
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

📝 Descripción
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
