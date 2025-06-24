import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { UserBasicDTO } from '../../dtos/userBasic.dto';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent implements OnInit {
  users: UserBasicDTO[] = [];
  currentPage = 0;
  loading = false;
  allLoaded = false;

  constructor(
    private userService: UserService,
    private loginService: LoginService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.checkAccess();
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

  private checkAccess(): void {
    this.loginService.reqIsLogged(); // Update el estado de autenticación

    // Verify if the user is logged in and is an admin
    setTimeout(() => {
      if (!this.loginService.isLogged()) {
        this.router.navigate(['/login']);
        return;
      }

      if (!this.loginService.isAdmin()) {
        this.router.navigate(['/error']);
        return;
      }

      this.loadUsers();
    }, 2000);
  }
}
