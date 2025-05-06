import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private apiUrl = 'https://localhost:8443/api/courses';

  constructor(private http: HttpClient) {}

  getCourses(page: number, size: number = 3) {
    return this.http.get<any[]>(`${this.apiUrl}/?page=${page}&size=${size}&sortBy=id`);
  }
}
