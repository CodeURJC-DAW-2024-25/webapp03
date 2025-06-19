import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { UserDTO } from '../../dtos/user.dto';

@Component({
  selector: 'app-modify-profile',
  templateUrl: './modify-profile.component.html',
  styleUrls: ['./modify-profile.component.css']
})
export class ModifyProfileComponent implements OnInit {
  profileForm!: FormGroup;
  selectedImageFile: File | null = null;
  previewImageUrl: string | null = null;
  user: UserDTO | undefined;
  originalEmail: string = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        this.user = user;
        this.originalEmail = user.email;

        this.profileForm = this.fb.group({
          name: [user.name],
          email: [user.email],
          password: [''],
        });

        this.previewImageUrl = `/api/users/${user.id}/image`;
      },
      error: (_) => {
        // Si no está autenticado, redirige al login
        this.router.navigate(['/login']);
      }
    });
  }

  onImageSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedImageFile = file;

      const reader = new FileReader();
      reader.onload = () => {
        this.previewImageUrl = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  onSubmit(): void {
    if (!this.user) return;

    const updateData: any = {
      name: this.profileForm.value.name,
      email: this.profileForm.value.email,
    };

    // Añade la password solo si está escrita
    if (this.profileForm.value.password) {
      updateData.password = this.profileForm.value.password;
    }

    // 1. Actualiza los datos del usuario (sin imagen)
    this.userService.updateUserData(this.user.id.toString(), updateData).subscribe({
      next: () => {
        // 2. Si hay imagen, súbela por separado
        if (this.selectedImageFile) {
          const formData = new FormData();
          formData.append('imageFile', this.selectedImageFile);

          this.userService.updateUserImage(this.user!.id.toString(), formData).subscribe({
            next: () => this.redirectAfterUpdate(),
            error: (err) => {
              console.error('Error al subir la imagen:', err);
              alert('Error al subir la imagen');
            }
          });
        } else {
          this.redirectAfterUpdate();
        }
      },
      error: (err) => {
        console.error('Error al guardar los cambios del usuario:', err);
        alert('Error al guardar los cambios');
      }
    });
  }

  redirectAfterUpdate(): void {
    const emailEdited = this.profileForm.value.email !== this.originalEmail;
    this.router.navigate([emailEdited ? '/login' : '/profile_page']);
  }
}
