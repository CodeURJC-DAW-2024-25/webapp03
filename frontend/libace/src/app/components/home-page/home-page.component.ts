import { Component, OnInit } from '@angular/core';
import { CourseService } from '../../services/course.service';
import { LoginService } from '../../services/login.service';

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
  filteredCourses: any[] = [];
  tagsInput: string = "";

  constructor(
    private courseService: CourseService,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.loadCourses();
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

  filter(): void {
    const tagsArray = this.tagsInput.split(',').map(tag => tag.trim()).filter(tag => tag);
    this.courseService.filterCoursesByTags(tagsArray, 0, 10).subscribe({
      next: (data) => {
        this.filteredCourses = data.content;
        this.courses = this.filteredCourses;
      },
      error: (err) => {
        console.error('Error al filtrar cursos:', err);
      }
    });
  }

  get isAdmin(): boolean {
    return this.loginService.isLogged();
  }

}
