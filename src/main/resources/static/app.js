// Configuración de AWS Cognito
const AWS_CONFIG = {
    region: 'us-east-1'
};

// Configurar AWS
AWS.config.update(AWS_CONFIG);

// Configuración de endpoints Lambda
const API_ENDPOINTS = {
    auth: 'https://q017exdbng.execute-api.us-east-1.amazonaws.com/dev/auth',
    posts: 'https://05k354jfek.execute-api.us-east-1.amazonaws.com/dev/posts',
    users: 'https://whiowbte9i.execute-api.us-east-1.amazonaws.com/dev/users'
};

// Estado de la aplicación
let currentUser = null;
let posts = [];

// Elementos del DOM
const loginModal = document.getElementById('loginModal');
const postsFeed = document.getElementById('postsFeed');
const loadingElement = document.getElementById('loading');
const postContent = document.getElementById('postContent');
const charCount = document.getElementById('charCount');
const navUser = document.getElementById('navUser');
const userCard = document.getElementById('userCard');

// Inicialización
document.addEventListener('DOMContentLoaded', function() {
    checkAuth();
    setupEventListeners();
});

// Verificar autenticación
async function checkAuth() {
    const token = localStorage.getItem('accessToken');
    
    if (!token) {
        showLoginModal();
        return;
    }

    try {
        await loadUserInfo(token);
        await loadFeed();
    } catch (error) {
        console.error('Error verificando autenticación:', error);
        localStorage.removeItem('accessToken');
        localStorage.removeItem('token');
        localStorage.removeItem('currentUser');
        showLoginModal();
    }
}

// Configurar event listeners
function setupEventListeners() {
    // Formularios de auth
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    document.getElementById('registerForm').addEventListener('submit', handleRegister);
    
    // Contador de caracteres
    postContent.addEventListener('input', updateCharCount);
    
    // Cerrar modal al hacer click fuera
    loginModal.addEventListener('click', function(e) {
        if (e.target === loginModal) {
            if (!currentUser) return;
            hideLoginModal();
        }
    });
}

// Actualizar contador de caracteres
function updateCharCount() {
    const length = postContent.value.length;
    charCount.textContent = `${length}/140`;
    
    charCount.className = 'char-count';
    if (length > 120) {
        charCount.classList.add('warning');
    }
    if (length > 140) {
        charCount.classList.add('error');
    }
}

// Manejar registro - VERSIÓN SIMPLIFICADA
async function handleRegister(e) {
    e.preventDefault();
    
    const displayName = document.getElementById('registerDisplayName').value;
    const email = document.getElementById('registerEmail').value;
    const password = document.getElementById('registerPassword').value;

    // Validaciones
    if (!displayName || !email || !password) {
        showNotification('Todos los campos son requeridos', 'error');
        return;
    }

    if (password.length < 8) {
        showNotification('La contraseña debe tener al menos 8 caracteres', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_ENDPOINTS.auth}/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: email,      // Email como username
                password: password,
                email: email,
                displayName: displayName
            })
        });

        const data = await response.json();

        if (response.ok) {
            showNotification('¡Usuario registrado exitosamente! Ya puedes iniciar sesión.', 'success');
            showAuthTab('login');
            document.getElementById('registerForm').reset();
        } else {
            throw new Error(data.error || 'Error en el registro');
        }
    } catch (error) {
        showNotification('Error en registro: ' + error.message, 'error');
    }
}

// Manejar login - VERSIÓN MEJORADA
async function handleLogin(e) {
    e.preventDefault();
    
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    if (!username || !password) {
        showNotification('Email y contraseña son requeridos', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_ENDPOINTS.auth}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        const data = await response.json();

        if (response.ok) {
            localStorage.setItem('token', data.token);
            localStorage.setItem('accessToken', data.accessToken);
            
            await loadUserInfo(data.accessToken);
            hideLoginModal();
            showNotification('¡Bienvenido!', 'success');
        } else {
            throw new Error(data.error || 'Error en el login');
        }
    } catch (error) {
        showNotification('Error en login: ' + error.message, 'error');
    }
}

// Cargar información del usuario
async function loadUserInfo(accessToken) {
    try {
        const response = await fetch(`${API_ENDPOINTS.users}/me`, {
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        });
        
        if (response.ok) {
            const userData = await response.json();
            currentUser = userData;
            localStorage.setItem('currentUser', JSON.stringify(userData));
            updateUI();
        } else {
            throw new Error('Error cargando información del usuario');
        }
    } catch (error) {
        console.error('Error cargando información del usuario:', error);
        throw error;
    }
}

// Cargar feed de posts
async function loadFeed() {
    showLoading();
    
    try {
        const token = localStorage.getItem('accessToken');
        const response = await fetch(API_ENDPOINTS.posts, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            posts = await response.json();
            renderPosts();
        } else {
            throw new Error('Error cargando posts');
        }
    } catch (error) {
        showNotification('Error cargando el feed: ' + error.message, 'error');
    } finally {
        hideLoading();
    }
}

// Crear nuevo post
async function createPost() {
    const content = postContent.value.trim();
    
    if (!content) {
        showNotification('Escribe algo para publicar', 'warning');
        return;
    }
    
    if (content.length > 140) {
        showNotification('El post no puede tener más de 140 caracteres', 'error');
        return;
    }
    
    try {
        const token = localStorage.getItem('accessToken');
        const user = JSON.parse(localStorage.getItem('currentUser'));
        
        const response = await fetch(API_ENDPOINTS.posts, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ 
                contenido: content,
                autor: user.username 
            })
        });
        
        const data = await response.json();
        
        if (response.ok) {
            postContent.value = '';
            updateCharCount();
            await loadFeed();
            showNotification('¡Post publicado!', 'success');
        } else {
            throw new Error(data.error || 'Error publicando el post');
        }
    } catch (error) {
        showNotification('Error publicando: ' + error.message, 'error');
    }
}

// Renderizar posts
function renderPosts() {
    if (posts.length === 0) {
        postsFeed.innerHTML = `
            <div class="post-card">
                <div style="text-align: center; padding: 2rem; color: #64748b;">
                    <i class="fas fa-comments" style="font-size: 3rem; margin-bottom: 1rem;"></i>
                    <h3>No hay posts aún</h3>
                    <p>Sé el primero en publicar algo</p>
                </div>
            </div>
        `;
        return;
    }
    
    postsFeed.innerHTML = posts.map(post => `
        <div class="post-card">
            <div class="post-header">
                <img src="https://ui-avatars.com/api/?name=${encodeURIComponent(post.autor)}&background=6366f1&color=fff" 
                     alt="Avatar" class="user-avatar">
                <div class="post-user-info">
                    <div class="post-username">${escapeHtml(post.autor)}</div>
                    <div class="post-time">${formatTime(post.createdAt)}</div>
                </div>
            </div>
            <div class="post-content">${escapeHtml(post.contenido)}</div>
            <div class="post-footer">
                <button class="post-action" onclick="likePost('${post.id}')">
                    <i class="far fa-heart"></i>
                    <span>Me gusta</span>
                </button>
                <button class="post-action" onclick="sharePost('${post.id}')">
                    <i class="far fa-share-square"></i>
                    <span>Compartir</span>
                </button>
            </div>
        </div>
    `).join('');
}

// Actualizar UI con información del usuario
function updateUI() {
    if (currentUser) {
        const displayName = currentUser.attributes?.name || currentUser.attributes?.email || currentUser.username;
        
        // Actualizar navbar
        navUser.innerHTML = `
            <div class="user-info">
                <img src="https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser.username)}&background=6366f1&color=fff" 
                     alt="Avatar" class="user-avatar">
                <span>@${currentUser.username}</span>
            </div>
        `;
        
        // Actualizar sidebar
        userCard.innerHTML = `
            <img src="https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser.username)}&background=6366f1&color=fff&size=80" 
                 alt="Avatar" class="user-card-avatar">
            <div class="user-card-name">${escapeHtml(displayName)}</div>
            <div class="user-card-username">@${escapeHtml(currentUser.username)}</div>
        `;
        
        // Actualizar avatar en crear post
        document.getElementById('currentUserAvatar').src = 
            `https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser.username)}&background=6366f1&color=fff`;
    }
}

// Funciones de UI
function showLoginModal() {
    loginModal.classList.remove('hidden');
}

function hideLoginModal() {
    loginModal.classList.add('hidden');
    document.getElementById('loginForm').reset();
    document.getElementById('registerForm').reset();
}

function showAuthTab(tab) {
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    document.querySelectorAll('.auth-form').forEach(form => form.classList.remove('active'));
    
    if (tab === 'login') {
        document.querySelector('.tab-btn:nth-child(1)').classList.add('active');
        document.getElementById('loginForm').classList.add('active');
    } else {
        document.querySelector('.tab-btn:nth-child(2)').classList.add('active');
        document.getElementById('registerForm').classList.add('active');
    }
}

function showSection(section) {
    console.log('Mostrar sección:', section);
}

function showLoading() {
    loadingElement.classList.remove('hidden');
}

function hideLoading() {
    loadingElement.classList.add('hidden');
}

// Cerrar sesión
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('accessToken');
    localStorage.removeItem('currentUser');
    currentUser = null;
    posts = [];
    showLoginModal();
    updateUI();
    showNotification('Sesión cerrada', 'info');
}

// Funciones de utilidad
function escapeHtml(unsafe) {
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

function formatTime(isoString) {
    const date = new Date(isoString);
    const now = new Date();
    const diffMs = now - date;
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    
    if (diffMins < 1) return 'Ahora mismo';
    if (diffMins < 60) return `Hace ${diffMins} min`;
    if (diffHours < 24) return `Hace ${diffHours} h`;
    
    return date.toLocaleDateString('es-ES', {
        day: 'numeric',
        month: 'short',
        year: 'numeric'
    });
}

function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <i class="fas fa-${getNotificationIcon(type)}"></i>
            <span>${message}</span>
        </div>
    `;
    
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: ${getNotificationColor(type)};
        color: white;
        padding: 1rem 1.5rem;
        border-radius: 10px;
        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
        z-index: 3000;
        animation: slideInRight 0.3s ease;
    `;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.style.animation = 'slideOutRight 0.3s ease';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 3000);
}

function getNotificationIcon(type) {
    const icons = {
        success: 'check-circle',
        error: 'exclamation-circle',
        warning: 'exclamation-triangle',
        info: 'info-circle'
    };
    return icons[type] || 'info-circle';
}

function getNotificationColor(type) {
    const colors = {
        success: '#10b981',
        error: '#ef4444',
        warning: '#f59e0b',
        info: '#6366f1'
    };
    return colors[type] || '#6366f1';
}

// Funciones de posts (placeholder)
function likePost(postId) {
    showNotification('Función de "Me gusta" próximamente', 'info');
}

function sharePost(postId) {
    showNotification('Función de "Compartir" próximamente', 'info');
}

// Agregar estilos de animación para notificaciones
const style = document.createElement('style');
style.textContent = `
    @keyframes slideInRight {
        from { transform: translateX(100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
    }
    
    @keyframes slideOutRight {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(100%); opacity: 0; }
    }
    
    .notification-content {
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }
`;
document.head.appendChild(style);