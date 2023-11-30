import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {MenuComponent} from './components/menu/menu.component';
import {AuthenticationComponent} from './components/authentication/authentication.component';
import {LoginComponent} from './components/login/login.component';
import {IndexComponent} from './components/index/index.component';
import {IndexContentComponent} from './components/index/index-content/index-content.component';
import {UserAccountComponent} from './components/menu/user-account/user-account.component';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {ProfileComponent} from './components/menu/profile/profile.component';
import {CreateCourseComponent} from './components/courses/create-course/create-course.component';
import {CKEditorModule} from '@ckeditor/ckeditor5-angular';
import {CourseViewComponent} from './components/courses/course-view/course-view.component';
import {CourseEditComponent} from './components/courses/course-edit/course-edit.component';
import {LanguagesComponent} from './components/languages/languages.component';
import {NgxSpinnerModule} from "ngx-spinner";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AddSectionComponent} from './components/courses/course-edit/add-section/add-section.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatDialogModule} from "@angular/material/dialog";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    AuthenticationComponent,
    LoginComponent,
    IndexComponent,
    IndexContentComponent,
    UserAccountComponent,
    ProfileComponent,
    CreateCourseComponent,
    CourseViewComponent,
    CourseEditComponent,
    LanguagesComponent,
    AddSectionComponent
  ],
  imports: [
    BrowserModule,
    CKEditorModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    FontAwesomeModule,
    BrowserAnimationsModule,
    NgxSpinnerModule,
    MatFormFieldModule,
    MatDialogModule,
    MatInputModule,
    MatButtonModule
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
