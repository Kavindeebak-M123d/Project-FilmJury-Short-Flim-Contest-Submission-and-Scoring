// FilmJury Frontend Script

document.addEventListener('DOMContentLoaded', () => {
    // Auto dismiss alert messages after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Score inputs live range indicator & total score calculator
    const scoreInputs = document.querySelectorAll('.score-input');
    if (scoreInputs.length > 0) {
        function updateTotal() {
            let total = 0;
            let count = 0;
            scoreInputs.forEach(input => {
                const val = parseFloat(input.value);
                if (!isNaN(val)) {
                    total += val;
                    count++;
                }
            });
            const avg = count > 0 ? (total / count).toFixed(1) : '0.0';
            const avgDisplay = document.getElementById('calculatedAverage');
            if (avgDisplay) {
                avgDisplay.textContent = avg;
            }
        }

        scoreInputs.forEach(input => {
            input.addEventListener('input', (e) => {
                const targetId = 'val_' + e.target.id;
                const valSpan = document.getElementById(targetId);
                if (valSpan) {
                    valSpan.textContent = e.target.value;
                }
                updateTotal();
            });
        });
        updateTotal();
    }
});
