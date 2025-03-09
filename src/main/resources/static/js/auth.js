// Check if token exists in localStorage or URL
document.addEventListener('DOMContentLoaded', function() {
    console.log('Auth.js loaded, checking for token...');
    
    // Check if we're on a protected page
    const isProtectedPage = window.location.pathname.includes('/dashboard') || 
                           window.location.pathname.includes('/admin') ||
                           window.location.pathname.includes('/donor') ||
                           window.location.pathname.includes('/volunteer') ||
                           window.location.pathname.includes('/affected');
    
    console.log('Is protected page:', isProtectedPage);
    
    if (isProtectedPage) {
        // First check URL for token
        const urlParams = new URLSearchParams(window.location.search);
        const tokenFromUrl = urlParams.get('token');
        
        if (tokenFromUrl) {
            console.log('Token found in URL');
            
            // Store token in localStorage
            localStorage.setItem('jwtToken', tokenFromUrl);
            
            // Store token in cookie
            document.cookie = `jwtToken=${tokenFromUrl}; path=/; max-age=86400`;
            
            // Clean URL by removing token parameter (optional)
            if (history.pushState) {
                const newUrl = window.location.protocol + "//" + 
                              window.location.host + 
                              window.location.pathname;
                window.history.pushState({path: newUrl}, '', newUrl);
                console.log('URL cleaned, token removed from URL');
            }
            
            // Add token to all fetch requests
            addTokenToFetchRequests();
        } else {
            // Check if token exists in localStorage
            const token = localStorage.getItem('jwtToken');
            
            if (token) {
                console.log('Token found in localStorage');
                
                // Ensure token is also in cookie
                document.cookie = `jwtToken=${token}; path=/; max-age=86400`;
                
                // Add token to all fetch requests
                addTokenToFetchRequests();
            } else {
                console.log('No token found, redirecting to login');
                // No token found, redirect to login
                window.location.href = '/login';
            }
        }
    }
});

// Function to add token to all fetch requests
function addTokenToFetchRequests() {
    const originalFetch = window.fetch;
    
    window.fetch = function(url, options = {}) {
        const token = localStorage.getItem('jwtToken');
        
        if (token) {
            options.headers = options.headers || {};
            options.headers['Authorization'] = `Bearer ${token}`;
        }
        
        return originalFetch(url, options);
    };
    
    console.log('Added token to all fetch requests');
}

// Function to add token to fetch requests
function fetchWithAuth(url, options = {}) {
    const token = localStorage.getItem('jwtToken');
    
    if (!token) {
        console.warn('No token found for authenticated request');
        return fetch(url, options);
    }
    
    // Create headers with Authorization
    const headers = options.headers || {};
    headers['Authorization'] = `Bearer ${token}`;
    
    console.log('Making authenticated request to:', url);
    
    return fetch(url, {
        ...options,
        headers
    });
}

// Function to logout
function logout() {
    console.log('Logging out...');
    
    // Clear token from localStorage
    localStorage.removeItem('jwtToken');
    
    // Clear token from cookies
    document.cookie = 'jwtToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    
    console.log('Tokens cleared, redirecting to home');
    
    // Redirect to home page
    window.location.href = '/';
} 