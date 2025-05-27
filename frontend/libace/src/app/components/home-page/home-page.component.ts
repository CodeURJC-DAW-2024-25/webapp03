import { Component, OnInit } from '@angular/core';
import { CourseService } from '../../services/course.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent implements OnInit {
  courses: any[] = [];
  currentPage = 0;
  loading = false;
  allLoaded = false;

  constructor(private courseService: CourseService) {}

  ngOnInit(): void {
    this.loadCourses();
    this.loadChart();
  }

  loadCourses() {
    if (this.loading || this.allLoaded) return;

    this.loading = true;

    this.courseService.getCourses(this.currentPage, 3).subscribe({
      next: (newCourses) => {
        if (newCourses.length === 0) {
          this.allLoaded = true;
        } else {
          this.courses.push(...newCourses);
          this.currentPage++;
        }
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando cursos', err);
        this.loading = false;
      }
    });
  }

  loadChart() {
    
  }
}
