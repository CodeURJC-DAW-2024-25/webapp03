import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { UserBasicDTO } from '../../dtos/userBasic.dto';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-admin-users',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent implements OnInit {
  deleteUser(arg0: number | undefined) {
    throw new Error('Method not implemented.');
  }
  users: UserBasicDTO[] = []; // Usamos UserBasicDTO aquí
  currentPage = 0;
  loading = false;
  allLoaded = false;

  constructor(
    private userService: UserService,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    if (this.loading || this.allLoaded) return;

    this.loading = true;
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
        this.loading = false;
      },
    });
  }

  get isAdmin(): boolean {
    return this.loginService.isAdmin();
  }
}
