import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { CourseComponent } from './components/course/course.component';
import { LoginComponent } from './components/login/login.component';
import { ProfilePageComponent } from './components/profile-page/profile-page.component';
import { RegisterComponent } from './components/register/register.component';
import { EditCourseComponent } from './components/edit-course/edit-course.component';
import { ModifyProfileComponent } from './components/modify-profile/modify-profile.component';
import { NewCourseComponent } from './components/new-course/new-course.component';
import { AdminUsersComponent } from './components/admin-users/admin-users.component';
import { CommentListComponent } from './components/comment-list/comment-list.component';
import { CourseListComponent } from './components/course-list/course-list.component';
import { MaterialListComponent } from './components/material-list/material-list.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { LoginErrorComponent } from './components/login-error/login-error.component';
import { RegisterErrorComponent } from './components/register-error/register-error.component';
import { ErrorComponent } from './components/error/error.component';
import { Error403Component } from './components/error403/error403.component';
import { Error404Component } from './components/error404/error404.component';
import { Error500Component } from './components/error500/error500.component';
import { ChartComponent } from './components/chart/chart.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    CourseComponent,
    LoginComponent,
    ProfilePageComponent,
    RegisterComponent,
    EditCourseComponent,
    ModifyProfileComponent,
    NewCourseComponent,
    AdminUsersComponent,
    CommentListComponent,
    CourseListComponent,
    MaterialListComponent,
    UserListComponent,
    HomePageComponent,
    LoginErrorComponent,
    RegisterErrorComponent,
    ErrorComponent,
    Error403Component,
    Error404Component,
    Error500Component,
    ChartComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
