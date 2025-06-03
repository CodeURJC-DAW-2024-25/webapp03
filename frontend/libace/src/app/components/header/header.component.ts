import { Component } from '@angular/core';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  constructor(
      private loginService: LoginService
    ) {}

  get logged(): boolean {
    return this.loginService.isLogged();
  }

  get userName(): string {
    return this.loginService.user?.email ?? '';
  }
}
