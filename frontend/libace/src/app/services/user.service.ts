import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { UserDTO } from '../dtos/user.dto';
import { UserBasicDTO } from '../dtos/userBasic.dto';
import { catchError, map, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = '/api/users';

  constructor(private http: HttpClient) {}

  getCurrentUser(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/me`, {
      withCredentials: true,
    });
  }

  getAllBasicUsers(page: number, size: number): Observable<UserBasicDTO[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http
      .get<{ content: UserBasicDTO[] }>(`${this.apiUrl}/`, {
        params,
        withCredentials: true,
      })
      .pipe(
        map((response) => response.content),
        catchError((error) => this.handleError(error))
      );
  }

  // Nuevo método para actualizar los datos del usuario
  updateUserData(
    userId: string,
    data: { name: string; email: string; password: string }
  ): Observable<any> {
    return this.http
      .put(`${this.apiUrl}/${userId}`, data, {
        withCredentials: true,
        responseType: 'text' as 'json', // Esto evita el intento de parsear JSON
      })
      .pipe(catchError((error) => this.handleError(error)));
  }

  // Nuevo método para subir la imagen
  updateUserImage(userId: string, imageData: FormData): Observable<any> {
    return this.http
      .put(`${this.apiUrl}/${userId}/image`, imageData, {
        withCredentials: true,
        responseType: 'text' as 'json',
      })
      .pipe(catchError((error) => this.handleError(error)));
  }

  private handleError(error: any) {
    console.error('error:', error);
    return throwError(
      () => new Error('Server Error (' + error.status + '): ' + error.message)
    );
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
