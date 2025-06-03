import { Component } from '@angular/core';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  constructor(
      private loginService: LoginService, private router: Router
    ) {}

  get logged(): boolean {
    return this.loginService.isLogged();
  }

  get userName(): string {
    return this.loginService.user?.email ?? '';
  }

  logOut() {
    this.loginService.logOut();
    this.router.navigate(['/']);
  }
}
