// Check if token exists and set up authentication header
document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('jwtToken');
    if (token) {
        // Set up axios default headers or fetch headers
        console.log('User is authenticated');
    }
    
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
    
    // Mobile sidebar toggle
    const sidebarToggle = document.getElementById('sidebarToggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            document.querySelector('.sidebar').classList.toggle('show');
        });
    }
    
    // Add active class to current nav item
    const currentLocation = window.location.pathname;
    const navLinks = document.querySelectorAll('.sidebar .nav-link');
    navLinks.forEach(link => {
        if (link.getAttribute('href') === currentLocation) {
            link.classList.add('active');
        }
    });
});

// Authentication functions
function login(username, password) {
    fetch('/api/auth/signin', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Login failed');
        }
    })
    .then(data => {
        // Store JWT token in localStorage
        localStorage.setItem('jwtToken', data.accessToken);
        localStorage.setItem('userRole', data.role);
        
        // Redirect to dashboard
        window.location.href = '/dashboard';
    })
    .catch(error => {
        console.error('Error:', error);
        // Show error message
        showAlert('error', 'Login failed. Please check your credentials.');
    });
}

function register(userData) {
    fetch('/api/auth/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            return response.json().then(data => {
                throw new Error(data.message || 'Registration failed');
            });
        }
    })
    .then(data => {
        // Show success message
        showAlert('success', 'Registration successful! You can now login.');
        
        // Redirect to login page after 2 seconds
        setTimeout(() => {
            window.location.href = '/login';
        }, 2000);
    })
    .catch(error => {
        console.error('Error:', error);
        // Show error message
        showAlert('error', error.message);
    });
}

function logout() {
    // Remove JWT token from localStorage
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('userRole');
    
    // Redirect to home page
    window.location.href = '/';
}

// Helper functions
function showAlert(type, message) {
    const alertContainer = document.getElementById('alertContainer');
    if (alertContainer) {
        const alertElement = document.createElement('div');
        alertElement.className = `alert alert-${type === 'error' ? 'danger' : 'success'} alert-dismissible fade show`;
        alertElement.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;
        alertContainer.innerHTML = '';
        alertContainer.appendChild(alertElement);
        
        // Auto dismiss after 5 seconds
        setTimeout(() => {
            const alert = bootstrap.Alert.getOrCreateInstance(alertElement);
            alert.close();
        }, 5000);
    }
}

// API request helper with JWT token
function apiRequest(url, method = 'GET', data = null) {
    const token = localStorage.getItem('jwtToken');
    const headers = {
        'Content-Type': 'application/json'
    };
    
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    
    const options = {
        method: method,
        headers: headers
    };
    
    if (data && (method === 'POST' || method === 'PUT')) {
        options.body = JSON.stringify(data);
    }
    
    return fetch(url, options)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                if (response.status === 401) {
                    // Token expired or invalid, redirect to login
                    logout();
                }
                throw new Error('API request failed');
            }
        });
} 