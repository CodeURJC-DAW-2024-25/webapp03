import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import { MaterialBasicDTO } from '../dtos/materialBasic.dto';
import { MaterialDTO } from '../dtos/material.dto';
import { map } from 'rxjs/operators';


@Injectable({
    providedIn: 'root'
})
export class MaterialService {
    private apiUrl = '/api/materials';

    constructor(private httpClient: HttpClient) { }

    getAllMaterialBasicDTOs(page: number, size: number): Observable<MaterialBasicDTO[]> {
        const params = new HttpParams()
            .set('page', page.toString())
            .set('size', size.toString());

        return this.httpClient.get<{ content: MaterialBasicDTO[] }>(`${this.apiUrl}/basic`, { params }).pipe(
            map(response => response.content),
            catchError(error => this.handleError(error))
        );
    }

    getMaterialById(id: number): Observable<MaterialDTO> {
        return this.httpClient.get<MaterialDTO>(`${this.apiUrl}/dto/${id}`).pipe(
            catchError(error => this.handleError(error))
        );
    }

    saveMaterial(material: any): Observable<void> {
        return this.httpClient.post<void>(`${this.apiUrl}/`, material).pipe(
            catchError(error => this.handleError(error))
        );
    }

    deleteMaterial(id: number): Observable<void> {
        return this.httpClient.delete<void>(`${this.apiUrl}/${id}`).pipe(
            catchError(error => this.handleError(error))
        );
    }

    getByCourseId(courseId: number, page: number, size: number): Observable<MaterialBasicDTO[]> {
        const params = new HttpParams()
            .set('page', page.toString())
            .set('size', size.toString());

        return this.httpClient.get<{ content: MaterialBasicDTO[] }>(`${this.apiUrl}/course/${courseId}/dto`, { params }).pipe(
            map(response => response.content),
            catchError(error => this.handleError(error))
        );
    }

    private handleError(error: any) {
        console.error("ERROR:", error);
        return throwError(() => new Error("Server error (" + error.status + "): " + error.statusText));
    }
}