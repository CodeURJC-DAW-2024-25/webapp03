import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { UserBasicDTO } from '../../dtos/userBasic.dto';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-users',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent implements OnInit {
  users: UserBasicDTO[] = []; // Usamos UserBasicDTO aquí
  currentPage = 0;
  loading = false;
  allLoaded = false;

  constructor(
    private userService: UserService,
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (!this.loginService.isLogged()) {
      this.router.navigate(['/login']); // Redirigir a login si no está logueado
      return;
    }

    // Verificar si el usuario es admin
    if (!this.loginService.isAdmin()) {
      this.router.navigate(['/error']); // Redirigir a página de error si no es admin
      return;
    }

    this.loadUsers();
  }

  loadUsers(): void {
    if (this.allLoaded) return;
    this.userService.getAllBasicUsers(this.currentPage, 3).subscribe({
      next: (newUsers) => {
        if (newUsers.length === 0) {
          this.allLoaded = true;
        } else {
          this.users.push(...newUsers);
          this.currentPage++;
        }
      },
      error: (err) => {
        console.error('Error loading users:', err);
      },
    });
  }

  get isAdmin(): boolean {
    return this.loginService.isAdmin();
  }

  deleteUser(userId: number): void {
    if (!this.isAdmin) return;
    this.userService.deleteUser(userId).subscribe({
      next: () => {
        this.users = this.users.filter((user) => user.id !== userId);
      },
      error: (err) => {
        console.error('Error deleting user:', err);
      },
    });
  }

  loadMore(): void {
    if (this.allLoaded) return;
    this.loadUsers();
  }
}
