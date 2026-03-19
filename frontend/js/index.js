const API = 'http://localhost:8081/api';

document.addEventListener('DOMContentLoaded', () => {

    if (sessionStorage.getItem('user')) {
        window.location.href = 'dashboard.html';
    }

    window.showTab = function(tab) {
        document.getElementById('form-login').style.display = tab === 'login' ? 'block' : 'none';
        document.getElementById('form-register').style.display = tab === 'register' ? 'block' : 'none';
        document.querySelectorAll('.tab-btn').forEach((btn, i) => {
            btn.classList.toggle('active', (tab === 'login' && i === 0) || (tab === 'register' && i === 1));
        });
        hideAlert();
    }

    function showAlert(message, type) {
        const alert = document.getElementById('alert');
        alert.textContent = message;
        alert.className = `alert alert-${type}`;
        alert.style.display = 'block';
    }

    function hideAlert() {
        document.getElementById('alert').style.display = 'none';
    }

    window.login = async function() {
        const email = document.getElementById('login-email').value.trim();
        const password = document.getElementById('login-password').value.trim();

        if (!email || !password) {
            showAlert('Completá todos los campos.', 'error');
            return;
        }

        try {
            const res = await fetch(`${API}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });

            const data = await res.json();

            if (res.ok) {
                sessionStorage.setItem('user', JSON.stringify(data));
                window.location.href = 'dashboard.html';
            } else {
                showAlert(data.message || 'Credenciales incorrectas.', 'error');
            }
        } catch (error) {
            showAlert('No se pudo conectar con el servidor.', 'error');
        }
    }

    window.register = async function() {
        const fullName = document.getElementById('reg-name').value.trim();
        const email = document.getElementById('reg-email').value.trim();
        const password = document.getElementById('reg-password').value.trim();

        if (!fullName || !email || !password) {
            showAlert('Completá todos los campos.', 'error');
            return;
        }

        try {
            const res = await fetch(`${API}/auth/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ fullName, email, password })
            });

            const data = await res.json();

            if (res.ok) {
                showAlert('¡Cuenta creada exitosamente! Ya podés iniciar sesión.', 'success');
                window.showTab('login');
            } else {
                showAlert(data.message || 'Error al registrarse.', 'error');
            }
        } catch (error) {
            showAlert('No se pudo conectar con el servidor.', 'error');
        }
    }
});