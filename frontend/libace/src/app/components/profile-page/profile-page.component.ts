import { Component, OnInit } from '@angular/core';
import { UserDTO } from '../../dtos/user.dto';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css'
})
export class ProfilePageComponent implements OnInit {

  user: UserDTO | undefined;


  constructor(private userService: UserService
    //, private router: Router
  ) { }


  ngOnInit(): void {
        this.userService.getCurrentUser().subscribe(user => this.user = user);

  }

}


