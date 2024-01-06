import {Component, OnInit} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";
import {AxiosService} from "../../../axios.service";
import {faGlobe} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-course-my-courses',
  templateUrl: './course-my-courses.component.html',
  styleUrls: ['./course-my-courses.component.css']
})
export class CourseMyCoursesComponent implements OnInit {
  isLoading: boolean = true;
  user: any = JSON.parse(localStorage.getItem("user") || '{}');
  internalization: any = JSON.parse(localStorage.getItem("internalization") || '{}');
  faGlobe = faGlobe

  userCourses: any = [];

  constructor(
    private spinner: NgxSpinnerService,
    private axiosService: AxiosService,
  ) {
  }

  ngOnInit(): void {
    this.spinner.show().then(r => {

      this.getListOfUserCourses();
    });
  }

  getListOfUserCourses() {
    //TODO get list of user courses
    this.axiosService.requestWithHeaderAuth(
      "GET",
      "user/courses?username=" + this.user.username,
      {},
      this.user.secure_TOKEN
    ).then((response: any) => {
      if (response) {
        this.userCourses = response.data;
        this.userCourses.forEach((course: any) => {
          this.getAuthorName(course).then((response: any) => {
            if (response) {
              course['authorName'] = response.data;
            }
          });
        })
        this.isLoading = false;
        this.spinner.hide();
      }
    }).catch((error) => {
      if (error) {
        this.spinner.hide();
        this.isLoading = false;
      }
    });
  }

  getAuthorName(course: any) {
    return this.axiosService.request(
      "GET",
      "/components/authorname?authorUsername=" + course.authorUsername,
      null
    );
  }

  protected readonly JSON = JSON;

  openCourse(url:string) {
    window.location.href = "/course/" + url;
  }
}
