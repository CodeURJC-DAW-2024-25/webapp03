import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import { CourseBasicDTO } from '../dtos/courseBasic.dto';
import { CourseInputDTO } from '../dtos/courseInput.dto';
import { map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private apiUrl = '/api/courses';

  constructor(private httpClient: HttpClient) { }

  public getCourses(page: number, size: number): Observable<CourseBasicDTO[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.httpClient.get<{ content: CourseBasicDTO[] }>(`${this.apiUrl}/`, { params }).pipe(
      map(response => response.content),
      catchError(error => this.handleError(error))
    );
  }

  getCourseById(id: number): Observable<CourseBasicDTO> {
    return this.httpClient.get<CourseBasicDTO>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => this.handleError(error))
    );
  }

  private handleError(error: any) {
    console.log("ERROR:");
    console.error(error);
    return throwError("Server error (" + error.status + "): " + error.text());
  }

  filterCoursesByTags(tags: string[], page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.httpClient.post<any>(`${this.apiUrl}/filter`, tags, { params });
  }

  createCourse(newCourse: CourseInputDTO): Observable<void> {
    return this.httpClient.post<void>(`${this.apiUrl}/`, newCourse);
  }

  deleteCourse(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.apiUrl}/${id}`);
  }

}
