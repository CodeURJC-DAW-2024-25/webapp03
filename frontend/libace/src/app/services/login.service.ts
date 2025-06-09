import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { UserBasicDTO } from "../dtos/userBasic.dto";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private apiUrl = '/api/auth';

  public logged: boolean = false;
  public user: UserBasicDTO | undefined = undefined;

  constructor(private httpClient: HttpClient) {
    this.reqIsLogged();
  }

  public reqIsLogged() {
    this.httpClient.get("/api/users/me", { withCredentials: true }).subscribe(
      (response) => {
        this.user = response as UserBasicDTO;
        this.logged = true;
      },
      (error) => {
        if (error.status != 404) {
          console.error(
            "Error when asking if logged: " + JSON.stringify(error)
          );
        }
      }
    );
  }

  register(name: string, email: string, password: string) {
    return this.httpClient.post('/api/users/', { name, email, password });
  }

  public logIn(user: string, pass: string) {
    return this.httpClient.post(
      this.apiUrl + "/login",
      { username: user, password: pass },
      { withCredentials: true }
    );
  }

  public logOut() {
    return this.httpClient
      .post(this.apiUrl + "/logout", { withCredentials: true })
      .subscribe((_) => {
        console.log("LOGOUT: Successfully");
        this.logged = false;
        this.user = undefined;
      });
  }

  public isLogged() {
    return this.logged;
  }

  public isAdmin() {
    return this.user?.email === "admin@gmail.com";
  }

  currentUser() {
    return this.user;
  }
}
