const AWS = require('aws-sdk');
const dynamodb = new AWS.DynamoDB.DocumentClient();

const TABLE_NAME = process.env.POSTS_TABLE;

exports.handler = async (event) => {
    console.log('Event:', JSON.stringify(event, null, 2));
    
    const headers = {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
        'Access-Control-Allow-Headers': 'Content-Type, Authorization, X-Amz-Date, X-Api-Key, X-Amz-Security-Token'
    };

    try {
        // Manejar preflight OPTIONS
        if (event.httpMethod === 'OPTIONS') {
            return {
                statusCode: 200,
                headers: headers,
                body: ''
            };
        }

        // Endpoint: Obtener todos los posts
        if (event.httpMethod === 'GET' && event.resource === '/posts') {
            const params = {
                TableName: TABLE_NAME,
                ScanIndexForward: false
            };

            const result = await dynamodb.scan(params).promise();
            
            // Ordenar por fecha de creación (más reciente primero)
            const sortedPosts = result.Items.sort((a, b) => 
                new Date(b.createdAt) - new Date(a.createdAt)
            );

            return {
                statusCode: 200,
                headers: headers,
                body: JSON.stringify(sortedPosts)
            };
        }

        // Endpoint: Crear nuevo post
        if (event.httpMethod === 'POST' && event.resource === '/posts') {
            const body = JSON.parse(event.body);
            const { contenido, autor } = body;

            if (!contenido || contenido.length > 140) {
                return {
                    statusCode: 400,
                    headers: headers,
                    body: JSON.stringify({ error: 'El contenido debe tener entre 1 y 140 caracteres' })
                };
            }

            const post = {
                id: Date.now().toString(),
                contenido: contenido,
                autor: autor,
                createdAt: new Date().toISOString(),
                updatedAt: new Date().toISOString()
            };

            await dynamodb.put({
                TableName: TABLE_NAME,
                Item: post
            }).promise();

            return {
                statusCode: 201,
                headers: headers,
                body: JSON.stringify(post)
            };
        }

        return {
            statusCode: 404,
            headers: headers,
            body: JSON.stringify({ error: 'Endpoint no encontrado' })
        };
        
    } catch (error) {
        console.error('Error:', error);
        return {
            statusCode: 500,
            headers: headers,
            body: JSON.stringify({ error: 'Error interno del servidor' })
        };
    }
};