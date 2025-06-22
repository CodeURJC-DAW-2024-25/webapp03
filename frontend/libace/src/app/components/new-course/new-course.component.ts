import { Component } from '@angular/core';
import { CourseService } from '../../services/course.service';
import { CourseInputDTO } from '../../dtos/courseInput.dto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-course',
  templateUrl: './new-course.component.html',
  styleUrl: './new-course.component.css'
})
export class NewCourseComponent {
  courseTitle = '';
  courseDescription = '';
  courseTags = '';
  
  constructor(private courseService: CourseService, private router: Router) {}

  createCourse() {
    const courseTagsList: string[] = this.courseTags
      .split(',')
      .map(tag => tag.trim())
      .filter(tag => tag.length > 0);
    const newCourse: CourseInputDTO = {
      title: this.courseTitle,
      description: this.courseDescription,
      tags: courseTagsList
    };
    this.courseService.createCourse(newCourse).subscribe({
      next: () => {
        this.router.navigate(['/']);
        },
      error: (err) => alert('Login fallido'),
    });
  }

}
