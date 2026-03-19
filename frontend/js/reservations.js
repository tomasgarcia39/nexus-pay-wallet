const API = 'http://localhost:8081/api';

document.addEventListener('DOMContentLoaded', () => {

    const user = JSON.parse(sessionStorage.getItem('user'));
    if (!user) window.location.href = 'index.html';

    document.getElementById('nav-username').textContent = user.fullName;

    function showAlert(id, message, type) {
        const alert = document.getElementById(id);
        alert.textContent = message;
        alert.className = `alert alert-${type}`;
        alert.style.display = 'block';
        setTimeout(() => alert.style.display = 'none', 5000);
    }

    async function loadBalance() {
        try {
            const res = await fetch(`${API}/wallet/${user.id}/balance`);
            const data = await res.json();
            document.getElementById('available-balance').textContent =
                parseFloat(data.availableBalance).toLocaleString('es-AR', { minimumFractionDigits: 2 });

            if (parseFloat(data.reservedBalance) > 0) {
                document.getElementById('balance-reserved').textContent =
                    `🔒 $${parseFloat(data.reservedBalance).toLocaleString('es-AR', { minimumFractionDigits: 2 })} reservado`;
            } else {
                document.getElementById('balance-reserved').textContent = '';
            }
        } catch (error) {
            console.error('Error cargando saldo:', error);
        }
    }

    window.createReservation = async function() {
        const description = document.getElementById('res-description').value.trim();
        const amount = parseFloat(document.getElementById('res-amount').value);

        if (!description) { showAlert('alert-create', 'Ingresá una descripción.', 'error'); return; }
        if (!amount || amount <= 0) { showAlert('alert-create', 'Ingresá un monto válido.', 'error'); return; }

        try {
            const res = await fetch(`${API}/reservations`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ userId: user.id, amount, description })
            });

            if (res.ok) {
                showAlert('alert-create', '¡Reserva creada exitosamente!', 'success');
                document.getElementById('res-description').value = '';
                document.getElementById('res-amount').value = '';
                loadBalance();
                loadReservations();
            } else {
                const data = await res.json();
                showAlert('alert-create', data.message || 'Error al crear la reserva.', 'error');
            }
        } catch (error) {
            showAlert('alert-create', 'No se pudo conectar con el servidor.', 'error');
        }
    }

    async function loadReservations() {
        try {
            const res = await fetch(`${API}/reservations/user/${user.id}`);
            const data = await res.json();
            const container = document.getElementById('reservations-list');

            if (data.length === 0) {
                container.innerHTML = `
                    <div class="empty-state">
                        <div class="empty-icon">🔒</div>
                        <p>No tenés reservas activas</p>
                    </div>`;
                return;
            }

            container.innerHTML = data.map(r => `
                <div class="reservation-item" id="res-${r.id}">
                    <div class="reservation-info">
                        <div class="reservation-desc">${r.description}</div>
                        <div class="reservation-amount">
                            $${parseFloat(r.amount).toLocaleString('es-AR', { minimumFractionDigits: 2 })}
                        </div>
                        <div style="margin-top:0.3rem;">
                            <span class="badge badge-${r.status.toLowerCase()}">${translateStatus(r.status)}</span>
                        </div>
                    </div>
                    ${r.status === 'PENDING' ? `
                    <div class="reservation-actions">
                        <button class="btn-confirm" onclick="confirmReservation(${r.id})">✓ Liberar dinero</button>
                    </div>` : ''}
                </div>
            `).join('');
        } catch (error) {
            console.error('Error cargando reservas:', error);
        }
    }

    window.confirmReservation = async function(id) {
        try {
            const res = await fetch(`${API}/reservations/${id}/confirm`, { method: 'PUT' });
            if (res.ok) {
                showAlert('alert-create', 'Reserva liberada. El dinero volvió a tu saldo disponible.', 'success');
                loadBalance();
                loadReservations();
            } else {
                const data = await res.json();
                showAlert('alert-create', data.message || 'Error al liberar la reserva.', 'error');
            }
        } catch (error) {
            showAlert('alert-create', 'No se pudo conectar con el servidor.', 'error');
        }
    }

    function translateStatus(status) {
        const map = { 'PENDING': 'Pendiente', 'CONFIRMED': 'Liberada', 'CANCELLED': 'Cancelada' };
        return map[status] || status;
    }

    window.logout = function() {
        sessionStorage.removeItem('user');
        window.location.href = 'index.html';
    }

    loadBalance();
    loadReservations();
});