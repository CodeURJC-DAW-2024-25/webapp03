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

  selectedImage: File | null = null;
  
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
      next: (createdCourse) => {
        if (this.selectedImage) {
          const imageFormData = new FormData();
          imageFormData.append('imageFile', this.selectedImage);
          this.courseService.uploadCourseImage(createdCourse.id, imageFormData).subscribe({
            next: () => this.router.navigate(['/']),
            error: (err) => alert('Error al subir imagen'),
          });
        } else {
          this.router.navigate(['/']);
        }
      },
      error: (err) => alert('Error al crear curso'),
    });
  }

  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedImage = input.files[0];
    }
  }

}
