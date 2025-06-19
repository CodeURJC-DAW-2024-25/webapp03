import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { UserDTO } from '../../dtos/user.dto';

@Component({
  selector: 'app-modify-profile',
  templateUrl: './modify-profile.component.html',
  styleUrls: ['./modify-profile.component.css']  // ojo: era "styleUrl", debe ser "styleUrls"
})
export class ModifyProfileComponent implements OnInit {
  profileForm!: FormGroup;
  selectedImageFile: File | null = null;
  previewImageUrl: string | null = null;
  user: UserDTO | undefined;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe(user => {
      this.user = user;

      this.profileForm = this.fb.group({
        name: [user.name],
        email: [user.email],
        password: [''],
      });

      this.previewImageUrl = `/api/users/${user.id}/image`;
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

    const formData = new FormData();
    formData.append('name', this.profileForm.value.name);
    formData.append('email', this.profileForm.value.email);
    if (this.profileForm.value.password) {
      formData.append('password', this.profileForm.value.password);
    }
    if (this.selectedImageFile) {
      formData.append('image', this.selectedImageFile);
    }

    this.userService.updateUser(this.user.id.toString(), formData).subscribe(() => {
      this.router.navigate(['/profile_page']);
    });
  }
}
