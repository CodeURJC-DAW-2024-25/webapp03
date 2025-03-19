document.addEventListener("DOMContentLoaded", function () {
    fetch('/chartData')
        .then(response => response.json())
        .then(chartData => {
            const labels = chartData.map(course => course.name);
            const values = chartData.map(course => course.inscriptions);

            const ctx = document.getElementById('courseChart').getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Usuarios Inscritos',
                        data: values,
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: { beginAtZero: true }
                    }
                }
            });
        });
});
