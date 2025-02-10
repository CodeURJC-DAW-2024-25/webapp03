document.addEventListener("DOMContentLoaded", function () {
    var ctx = document.getElementById("courseChart").getContext("2d");
    var courseChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: ["Papiroflexia", "Dibujo", "Finish", "Lines", "Southwest", "Window"],
            datasets: [{
                label: "Usuarios por Curso",
                data: [120, 200, 150, 180, 90, 50],
                backgroundColor: ["#4e73df", "#1cc88a", "#36b9cc", "#f6c23e", "#e74a3b", "#396843"],
                borderColor: "#ddd",
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
});