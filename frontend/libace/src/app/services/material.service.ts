import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpEventType, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, last } from 'rxjs/operators';
import { MaterialBasicDTO } from '../dtos/materialBasic.dto';

@Injectable({
    providedIn: 'root'
})
export class MaterialService {
    private apiUrl = '/api/materials';

    constructor(private httpClient: HttpClient) { }

    getAllBasicDTOs(page: number, size: number): Observable<MaterialBasicDTO[]> {
        const params = new HttpParams()
            .set('page', page.toString())
            .set('size', size.toString());

        return this.httpClient.get<{ content: MaterialBasicDTO[] }>(`${this.apiUrl}/`, {
            params,
            withCredentials: true
        }).pipe(
            map(response => response.content),
            catchError(error => this.handleError(error))
        );
    }

    uploadFile(courseId: number, file: File): Observable<MaterialBasicDTO> {
        const formData = new FormData();
        formData.append('file', file);

        return this.httpClient.post<MaterialBasicDTO>(`${this.apiUrl}/courses/${courseId}/`, formData, {
            withCredentials: true
        }).pipe(
            catchError(error => this.handleError(error))
        );
    }

    downloadFile(courseId: number, materialId: number): Observable<Blob> {
        return this.httpClient.get(`${this.apiUrl}/courses/${courseId}/materials/${materialId}/`, {
            responseType: 'blob',
            withCredentials: true
        }).pipe(
            catchError(error => this.handleError(error))
        );
    }

    deleteFile(courseId: number, materialId: number): Observable<void> {
        return this.httpClient.delete<void>(`${this.apiUrl}/courses/${courseId}/materials/${materialId}/`, {
            withCredentials: true
        }).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private handleError(error: any) {
        console.error('MaterialService error:', error);
        return throwError(() => new Error("Server error (" + error.status + "): " + error.message));
    }
}