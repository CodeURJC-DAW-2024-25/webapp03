import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentBasicDTO } from '../dtos/commentBasic.dto';

@Injectable({
    providedIn: 'root'
})
export class CommentService {
    private apiUrl = '/api/courses';

    constructor(private http: HttpClient) { }

    getComments(courseId: number): Observable<CommentBasicDTO[]> {
        return this.http.get<CommentBasicDTO[]>(`${this.apiUrl}/${courseId}/comments`);
    }

    addComment(courseId: number, text: string): Observable<CommentBasicDTO> {
        return this.http.post<CommentBasicDTO>(`${this.apiUrl}/${courseId}/comments/`, { text });
    }

    deleteComment(courseId: number, commentId: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${courseId}/comments/${commentId}`);
    }
}