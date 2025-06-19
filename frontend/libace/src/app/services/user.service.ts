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
    }); //Really needed for cookies? TODO
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

  updateUser(userId: string, data: FormData) {
    return this.http.put(`/api/users/${userId}`, data);
  }

  private handleError(error: any) {
    console.log('error:');
    console.error(error);
    return throwError('Server Error (' + error.status + '): ' + error.text());
  }
}
