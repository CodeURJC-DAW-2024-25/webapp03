import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CourseBasicDTO } from '../../dtos/courseBasic.dto';
import { MaterialBasicDTO } from '../../dtos/materialBasic.dto';
import { CourseService } from '../../services/course.service';
import { MaterialService } from '../../services/material.service';
import { LoginService } from '../../services/login.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})
export class CourseComponent implements OnInit {

  course!: CourseBasicDTO;
  materials: MaterialBasicDTO[] = [];

  newCommentText: string = '';
  selectedFile?: File;

  courseId!: number;

  pageMaterial: number = 0;
  pageComment: number = 0;
  pageSize: number = 5;

  loadingMaterials = false;
  loadingComments = false;

  logged = false;
  admin = false;

  private userSub!: Subscription;

  constructor(
    private route: ActivatedRoute,
    private courseService: CourseService,
    private materialService: MaterialService,
    private loginService: LoginService
  ) { }

  ngOnInit(): void {
    this.courseId = Number(this.route.snapshot.paramMap.get('id'));

    this.logged = this.loginService.isLogged();
    this.admin = this.loginService.isAdmin();

    this.loadCourse();
    this.loadMaterials();
  }

  ngOnDestroy(): void {
    if (this.userSub) {
      this.userSub.unsubscribe();
    }
  }

  loadCourse(): void {
    this.courseService.getCourseById(this.courseId).subscribe({
      next: (data) => this.course = data,
      error: (err) => console.error('Error cargando curso', err)
    });
  }

  loadMaterials(): void {
    this.loadingMaterials = true;
    this.materialService.getByCourseId(this.courseId, this.pageMaterial, this.pageSize).subscribe({
      next: (data) => {
        this.materials = this.materials.concat(data);
        this.pageMaterial++;
        this.loadingMaterials = false;
      },
      error: (err) => {
        console.error('Error cargando materiales', err);
        this.loadingMaterials = false;
      }
    });
  }

  loadComments(): void {
    // Tu lógica de comentarios (si decides añadirla)
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  onUploadMaterial(): void {
    if (!this.selectedFile) return;
    this.materialService.uploadFile(this.courseId, this.selectedFile).subscribe({
      next: () => {
        this.pageMaterial = 0;
        this.materials = [];
        this.loadMaterials(); // recarga desde cero
      },
      error: (err) => console.error('Error subiendo material', err)
    });
  }

  onDeleteMaterial(id: number): void {
    this.materialService.deleteFile(this.courseId, id).subscribe({
      next: () => {
        this.materials = this.materials.filter(m => m.id !== id);
      },
      error: (err) => console.error('Error eliminando material', err)
    });
  }

  onSubmitComment(): void {
    // Lógica para añadir comentario
  }

  onDeleteComment(id: number): void {
    // Lógica para eliminar comentario
  }

  loadMoreMaterials(): void {
    this.loadMaterials();
  }

  loadMoreComments(): void {
    this.loadComments();
  }
}
