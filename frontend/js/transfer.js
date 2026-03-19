const API = 'http://localhost:8081/api';

document.addEventListener('DOMContentLoaded', () => {

    const user = JSON.parse(sessionStorage.getItem('user'));
    if (!user) window.location.href = 'index.html';

    document.getElementById('nav-username').textContent = user.fullName;

    async function loadBalance() {
        try {
            const res = await fetch(`${API}/wallet/${user.id}/balance`);
            const data = await res.json();
            document.getElementById('available-balance').textContent =
                parseFloat(data.availableBalance).toLocaleString('es-AR', { minimumFractionDigits: 2 });
        } catch (error) {
            console.error('Error cargando saldo:', error);
        }
    }

    function showAlert(message, type) {
        const alert = document.getElementById('alert');
        alert.textContent = message;
        alert.className = `alert alert-${type}`;
        alert.style.display = 'block';
        setTimeout(() => alert.style.display = 'none', 5000);
    }

    window.transfer = function() {
        const account = document.getElementById('receiver-account').value.trim();
        const amount = parseFloat(document.getElementById('amount').value);

        if (!account) { showAlert('Ingresá el número de cuenta destino.', 'error'); return; }
        if (!amount || amount <= 0) { showAlert('Ingresá un monto válido.', 'error'); return; }
        if (account === user.accountNumber) { showAlert('No podés transferirte dinero a vos mismo.', 'error'); return; }

        document.getElementById('confirm-account').textContent = account;
        document.getElementById('confirm-amount').textContent =
            '$' + amount.toLocaleString('es-AR', { minimumFractionDigits: 2 });
        document.getElementById('confirm-modal').style.display = 'flex';
    }

    window.closeModal = function() {
        document.getElementById('confirm-modal').style.display = 'none';
    }

    window.confirmTransfer = async function() {
        const account = document.getElementById('receiver-account').value.trim();
        const amount = parseFloat(document.getElementById('amount').value);
        window.closeModal();

        const btn = document.getElementById('btn-transfer');
        btn.textContent = 'Procesando...';
        btn.disabled = true;

        try {
            const res = await fetch(`${API}/wallet/transfer`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ senderUserId: user.id, receiverAccountNumber: account, amount })
            });

            if (res.ok) {
                showAlert('¡Transferencia realizada exitosamente!', 'success');
                document.getElementById('receiver-account').value = '';
                document.getElementById('amount').value = '';
                loadBalance();
            } else {
                const data = await res.json();
                showAlert(data.message || 'Error al realizar la transferencia.', 'error');
            }
        } catch (error) {
            showAlert('No se pudo conectar con el servidor.', 'error');
        } finally {
            btn.textContent = 'Enviar dinero 💸';
            btn.disabled = false;
        }
    }

    window.logout = function() {
        sessionStorage.removeItem('user');
        window.location.href = 'index.html';
    }

    loadBalance();
});