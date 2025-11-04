const AWS = require('aws-sdk');
const cognito = new AWS.CognitoIdentityServiceProvider();

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

        // Endpoint: Obtener información del usuario actual
        if (event.httpMethod === 'GET' && event.resource === '/users/me') {
            const token = event.headers.Authorization || event.headers.authorization;
            
            if (!token) {
                return {
                    statusCode: 401,
                    headers: headers,
                    body: JSON.stringify({ error: 'Token de autorización requerido' })
                };
            }

            // Remover 'Bearer ' si está presente
            const accessToken = token.startsWith('Bearer ') ? token.replace('Bearer ', '') : token;

            try {
                const userInfo = await cognito.getUser({ AccessToken: accessToken }).promise();
                
                const userData = {
                    username: userInfo.Username,
                    attributes: {}
                };
                
                userInfo.UserAttributes.forEach(attr => {
                    userData.attributes[attr.Name] = attr.Value;
                });

                return {
                    statusCode: 200,
                    headers: headers,
                    body: JSON.stringify(userData)
                };
            } catch (error) {
                console.error('Cognito error:', error);
                return {
                    statusCode: 401,
                    headers: headers,
                    body: JSON.stringify({ error: 'Token inválido o expirado: ' + error.message })
                };
            }
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