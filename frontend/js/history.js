const API = 'http://localhost:8081/api';

document.addEventListener('DOMContentLoaded', () => {

    const user = JSON.parse(sessionStorage.getItem('user'));
    if (!user) window.location.href = 'index.html';

    document.getElementById('nav-username').textContent = user.fullName;

    let allTransactions = [];
    let currentFilter = 'ALL';

    async function loadTransactions() {
        try {
            const res = await fetch(`${API}/wallet/${user.id}/transactions`);
            allTransactions = await res.json();

            let totalCredit = 0;
            let totalDebit = 0;

            allTransactions.forEach(tx => {
                if (tx.type === 'CREDIT') totalCredit += parseFloat(tx.amount);
                else totalDebit += parseFloat(tx.amount);
            });

            document.getElementById('total-credit').textContent =
                totalCredit.toLocaleString('es-AR', { minimumFractionDigits: 2 });
            document.getElementById('total-debit').textContent =
                totalDebit.toLocaleString('es-AR', { minimumFractionDigits: 2 });

            renderTransactions();
        } catch (error) {
            console.error('Error cargando historial:', error);
        }
    }

    function renderTransactions() {
        const filtered = currentFilter === 'ALL'
            ? allTransactions
            : allTransactions.filter(tx => tx.type === currentFilter);

        const container = document.getElementById('transactions-list');

        if (filtered.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <div class="empty-icon">📭</div>
                    <p>No hay movimientos para mostrar</p>
                </div>`;
            return;
        }

        container.innerHTML = filtered.map(tx => `
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
    }

    window.filterTransactions = function(type) {
        currentFilter = type;
        document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
        event.target.classList.add('active');
        renderTransactions();
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

    loadTransactions();
});