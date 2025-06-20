import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserDTO } from '../../dtos/user.dto';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css'
})
export class ProfilePageComponent implements OnInit {

  user: UserDTO | undefined;
  userImageUrl: string | undefined;

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        this.user = user;
        if (this.user) {
          this.userService.getUserImage(this.user.id.toString()).subscribe({
            next: (blob) => {
              this.userImageUrl = URL.createObjectURL(blob);
            },
            error: (err) => {
              console.error('Error loading user image:', err);
            }
          });
        }
      },
      error: (_) => {
        // Si no está autenticado, redirige al login
        this.router.navigate(['/login']);
      }
    });
  }
}
