import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {IndexComponent} from "./components/index/index.component";
import {LoginComponent} from "./components/login/login.component";
import {CreateCourseComponent} from "./components/courses/create-course/create-course.component";
import {CourseViewComponent} from "./components/courses/course-view/course-view.component";
import {CourseEditComponent} from "./components/courses/course-edit/course-edit.component";
import {CourseViewVideoComponent} from "./components/courses/course-view/course-view-video/course-view-video.component";

const routes: Routes = [
  {path: '', component: IndexComponent},
  {path: 'login', component: LoginComponent},
  {path: 'course-create', component: CreateCourseComponent},
  {path: 'course/:name', component: CourseViewComponent},
  {path: 'course/:name/video/:url', component: CourseViewVideoComponent},
  {path: 'course/:name/edit', component: CourseEditComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
