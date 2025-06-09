import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { UserDTO } from "../dtos/user.dto";
import { Observable } from "rxjs";


@Injectable({
  providedIn: 'root'
})

export class UserService {
  private apiUrl = '/api/users';

  constructor(private http: HttpClient) { }

  getCurrentUser(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/me`, { withCredentials: true }); //Really needed for cookies? TODO
  }

}



