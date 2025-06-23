import { Component } from '@angular/core';
import { CourseService } from '../../services/course.service';
import { LoginService } from '../../services/login.service';
import { CourseInputDTO } from '../../dtos/courseInput.dto';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-edit-course',
  templateUrl: './edit-course.component.html',
  styleUrl: './edit-course.component.css'
})
export class EditCourseComponent {
  courseId = 0;
  courseTitle = '';
  courseDescription = '';
  courseTags: string[] = [];
  removeImage = false;
  oldImage: File | null = null;

  selectedImage: File | null = null;
  
  constructor(private courseService: CourseService, private loginService: LoginService, private router: Router, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.checkAccess();
    const courseId = Number(this.route.snapshot.paramMap.get('id'));
    if (courseId) {
      this.courseId = courseId;
      this.loadCourse(courseId);
    }
  }

  loadCourse(id: number): void {
  this.courseService.getCourseById(id).subscribe(course => {
    this.courseTitle = course.title;
    this.courseDescription = course.description;
    this.courseTags = course.tags;
  });
  this.courseService.getCourseImage(id).subscribe(blob => {
    const file = new File([blob], 'original.jpg', { type: blob.type });
    this.oldImage = file;
  });
}

  editCourse() {

    const courseToEdit: CourseInputDTO = {
      title: this.courseTitle,
      description: this.courseDescription,
      tags: this.courseTags
    };

    this.courseService.editCourse(courseToEdit, this.courseId).subscribe({
      next: (editedCourse) => {
        if (this.selectedImage) {
          const imageFormData = new FormData();
          imageFormData.append('imageFile', this.selectedImage);
          this.courseService.uploadCourseImage(editedCourse.id, imageFormData).subscribe({
            next: () => this.router.navigate(['/']),
            error: (err) => alert('Error al subir imagen'),
          });
        } else if (this.removeImage){
          this.courseService.deleteCourseImage(editedCourse.id).subscribe({
            next: () => this.router.navigate(['/']),
            error: (err) => alert('Error al borrar la imagen')
          })
        } else if (this.oldImage) {
          const oldImageFormData = new FormData();
          oldImageFormData.append('imageFile', this.oldImage);
          this.courseService.uploadCourseImage(editedCourse.id, oldImageFormData).subscribe({
            next: () => this.router.navigate(['/']),
            error: (err) => alert('Error al subir imagen sin modificar'),
          });
        } else {
          this.router.navigate(['/'])
        }
      },
      error: (err) => alert('Error al editar curso'),
    });
  }

  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedImage = input.files[0];
    }
  }

  private checkAccess(): void {
    this.loginService.reqIsLogged();

    setTimeout(() => {
      if (!this.loginService.isLogged()) {
        this.router.navigate(['/login']);
        return;
      }

      if (!this.loginService.isAdmin()) {
        this.router.navigate(['/error']);
        return;
      }

    }, 2000);
  }
}
