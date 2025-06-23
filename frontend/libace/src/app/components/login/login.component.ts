import { Component } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  username = '';
  password = '';

  constructor(private loginService: LoginService, private router: Router) {}

  onSubmit() {
    this.loginService.logIn(this.username, this.password).subscribe({
      next: () => {
        this.loginService.reqIsLogged();
        this.router.navigate(['/']);
      },
      error: (err) => this.router.navigate(['/error']),
    });
  }
}
