const API = 'http://localhost:8081/api';

document.addEventListener('DOMContentLoaded', () => {

    const user = JSON.parse(sessionStorage.getItem('user'));
    if (!user) window.location.href = 'index.html';

    document.getElementById('nav-username').textContent = user.fullName;

    async function loadBalance() {
        try {
            const res = await fetch(`${API}/wallet/${user.id}/balance`);
            const data = await res.json();

            document.getElementById('balance-amount').textContent =
                parseFloat(data.availableBalance).toLocaleString('es-AR', { minimumFractionDigits: 2 });
            document.getElementById('account-number').textContent = '📇 ' + data.accountNumber;

            if (parseFloat(data.reservedBalance) > 0) {
                document.getElementById('balance-reserved').textContent =
                    `🔒 $${parseFloat(data.reservedBalance).toLocaleString('es-AR', { minimumFractionDigits: 2 })} reservado`;
            }
        } catch (error) {
            console.error('Error cargando saldo:', error);
        }
    }

    async function loadTransactions() {
        try {
            const res = await fetch(`${API}/wallet/${user.id}/transactions`);
            const data = await res.json();
            const container = document.getElementById('transactions-list');

            if (data.length === 0) {
                container.innerHTML = `
                    <div class="empty-state">
                        <div class="empty-icon">📭</div>
                        <p>No hay movimientos todavía</p>
                    </div>`;
                return;
            }

            const recent = data.slice(0, 5);
            container.innerHTML = recent.map(tx => `
                <div class="transaction-item">
                    <div class="transaction-icon ${tx.type === 'CREDIT' ? 'credit' : 'debit'}">
                        ${tx.type === 'CREDIT' ? '↓' : '↑'}
                    </div>
                    <div class="transaction-info">
                        <div class="transaction-desc">${tx.description}</div>
                        <div class="transaction-date">${formatDate(tx.timestamp)}</div>
                    </div>
                    <div class="transaction-amount ${tx.type === 'CREDIT' ? 'credit' : 'debit'}">
                        ${tx.type === 'CREDIT' ? '+' : '-'}$${parseFloat(tx.amount).toLocaleString('es-AR', { minimumFractionDigits: 2 })}
                    </div>
                </div>
            `).join('');
        } catch (error) {
            console.error('Error cargando transacciones:', error);
        }
    }

    function formatDate(timestamp) {
        const date = new Date(timestamp);
        return date.toLocaleDateString('es-AR', {
            day: '2-digit', month: 'short', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    }

    window.logout = function() {
        sessionStorage.removeItem('user');
        window.location.href = 'index.html';
    }

    loadBalance();
    loadTransactions();
});