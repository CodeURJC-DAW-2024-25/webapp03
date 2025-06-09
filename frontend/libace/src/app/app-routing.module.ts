import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { NewCourseComponent } from './components/new-course/new-course.component';
import { CourseComponent } from './components/course/course.component';
import { EditCourseComponent } from './components/edit-course/edit-course.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { ProfilePageComponent } from './components/profile-page/profile-page.component';

const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'newcourse', component: NewCourseComponent },
  { path: 'courses/:id', component: CourseComponent },
  { path: 'editcourse/:id', component: EditCourseComponent },
  { path: 'admin/users', component: UserListComponent },
  { path: 'profile_page', component: ProfilePageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
