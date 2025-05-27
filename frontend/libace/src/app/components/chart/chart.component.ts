import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Chart, ChartConfiguration, ChartType, BarElement, BarController, CategoryScale, LinearScale, Tooltip, Legend, Title } from 'chart.js';
import { ChartService } from '../../services/chart.service';
import { ChartDataItemDTO } from '../../dtos/chartDataItem.dto';

Chart.register(BarElement, BarController, CategoryScale, LinearScale, Tooltip, Legend, Title);

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrl: './chart.component.css'
})

export class ChartComponent implements OnInit {
  @ViewChild('ChartCanvas', { static: true }) chartRef!: ElementRef<HTMLCanvasElement>;
  chart!: Chart;
  loading = true;

  constructor(private chartService: ChartService) {}

  ngOnInit(): void {
    this.chartService.getChartData().subscribe({
      next: (data: ChartDataItemDTO[]) => {
        const labels = data.map(d => d.name);
        const values = data.map(d => d.inscriptions);

        const config: ChartConfiguration = {
          type: 'bar' as ChartType,
          data: {
            labels,
            datasets: [
              {
                label: 'Inscripciones por curso',
                data: values,
                backgroundColor: 'rgba(54, 162, 235, 0.5)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
              }
            ]
          },
          options: {
            responsive: true,
            plugins: {
              legend: {
                display: true
              }
            },
            scales: {
              y: {
                beginAtZero: true
              }
            }
          }
        };

        this.chart = new Chart(this.chartRef.nativeElement, config);
        this.loading = false;
      },
      error: err => {
        console.error('Error al cargar datos del gráfico', err);
        this.loading = false;
      }
    });
  }
}
