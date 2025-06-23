import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  name = '';
  email = '';
  password = '';
  error = '';

  constructor(private loginService: LoginService, private router: Router) {}

  onSubmit() {
    this.loginService.register(this.name, this.email, this.password).subscribe({
      next: () => this.router.navigate(['/login']),
      error: (err) => {
        if (err.status === 409) {
          this.error = 'El usuario ya existe.';
        } else {
          this.error = 'Error en el registro.';
        }
      }
    });
  }
}
