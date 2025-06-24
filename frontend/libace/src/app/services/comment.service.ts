import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { CommentDTO } from '../dtos/comment.dto';


@Injectable({
    providedIn: 'root'
})
export class CommentService {
    private apiUrl = '/api/courses';

    constructor(private http: HttpClient) { }

    getCommentsByCourse(courseId: number, page: number, size: number): Observable<CommentDTO[]> {
        return this.http.get<{ content: CommentDTO[] }>(`/api/courses/${courseId}/comments`, {
            params: {
                page: page.toString(),
                size: size.toString()
            }
        }).pipe(
            map(response => response.content)
        );
    }
    addComment(courseId: number, text: string): Observable<CommentDTO> {
        return this.http.post<CommentDTO>(`${this.apiUrl}/${courseId}/comments/`, { text });
    }

    deleteComment(courseId: number, commentId: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${courseId}/comments/${commentId}`);
    }
}