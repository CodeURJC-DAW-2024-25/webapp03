import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CourseBasicDTO } from '../../dtos/courseBasic.dto';
import { MaterialBasicDTO } from '../../dtos/materialBasic.dto';
import { CourseService } from '../../services/course.service';
import { MaterialService } from '../../services/material.service';
import { LoginService } from '../../services/login.service';
import { CommentService } from '../../services/comment.service';
import { CommentDTO } from '../../dtos/comment.dto';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})
export class CourseComponent implements OnInit {

  course!: CourseBasicDTO;
  materials: MaterialBasicDTO[] = [];

  comments: CommentDTO[] = [];
  newCommentText: string = '';

  selectedFile?: File;

  courseId!: number;

  pageMaterial: number = 0;
  pageComment: number = 0;
  pageSizeMaterial: number = 3;
  pageSizeComment: number = 5;

  loadingMaterials = false;
  loadingComments = false;

  get isLogged(): boolean {
    return this.loginService.isLogged();
  }

  get isAdmin(): boolean {
    return this.loginService.isAdmin();
  }

  constructor(
    private route: ActivatedRoute,
    private courseService: CourseService,
    private materialService: MaterialService,
    private loginService: LoginService,
    private commentService: CommentService,
  ) { }

  ngOnInit(): void {
    this.courseId = Number(this.route.snapshot.paramMap.get('id'));

    this.loadCourse();
    this.loadMaterials();
    this.loadComments();
  }

  loadCourse(): void {
    this.courseService.getCourseById(this.courseId).subscribe({
      next: (data) => this.course = data,
      error: (err) => console.error('Error cargando curso', err)
    });
  }

  loadMaterials(): void {
    this.loadingMaterials = true;
    this.materialService.getByCourseId(this.courseId, this.pageMaterial, this.pageSizeMaterial).subscribe({
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
    this.loadingComments = true;
    this.commentService.getCommentsByCourse(this.courseId, this.pageComment, this.pageSizeComment).subscribe({
      next: (comments) => {
        this.comments = this.comments.concat(comments);
        this.pageComment++;
        this.loadingComments = false;
      },
      error: (err) => {
        console.error('Error cargando comentarios', err);
        this.loadingComments = false;
      }
    });
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
        this.loadMaterials();
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
    if (!this.newCommentText.trim()) return;

    this.commentService.addComment(this.courseId, this.newCommentText).subscribe({
      next: () => {
        this.pageComment = 0;
        this.comments = [];
        this.loadComments();
        this.newCommentText = '';
      },
      error: (err) => console.error('Error al añadir comentario', err)
    });
  }

  onDeleteComment(id: number): void {
    this.commentService.deleteComment(this.courseId, id).subscribe({
      next: () => {
        this.pageComment = 0;
        this.comments = [];
        this.loadComments();
      },
      error: (err) => console.error('Error eliminando comentario', err)
    });
  }

  loadMoreMaterials(): void {
    this.loadMaterials();
  }

  loadMoreComments(): void {
    this.loadComments();
  }
}
