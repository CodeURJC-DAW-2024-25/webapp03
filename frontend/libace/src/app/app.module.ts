import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
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
import { UserListComponent } from './components/user-list/user-list.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { ErrorComponent } from './components/error/error.component';
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
    UserListComponent,
    HomePageComponent,
    ErrorComponent,
    ChartComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
