import {Component, OnInit} from '@angular/core';
import Editor from 'ckeditor5-custom-build';
import {AxiosService} from "../../../axios.service";
import {DataService} from "../../../data.service";
import {Title} from "@angular/platform-browser";
import {NgxSpinnerService} from "ngx-spinner";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-course',
  templateUrl: './create-course.component.html',
  styleUrls: ['./create-course.component.css']
})
export class CreateCourseComponent implements OnInit {
  isPageLoading = false;
  Editor = Editor;
  user: any;
  languages: any;
  isUpdate = false;
  courseDescription: String = '';
  courseShortDescription: String = '';
  courseName: String = '';
  courseLanguage: String = '';
  courseCategory: String = '';
  coursePolicy: boolean = true;
  courseUrl: String = '';
  error: String = '';


  constructor(private axiosService: AxiosService,
              private data: DataService,
              private titleService: Title,
              private spinner: NgxSpinnerService,
              private router: Router) {
    this.titleService.setTitle("Create course");
  }

  ngOnInit(): void {
    this.isPageLoading = true;
    this.spinner.show().then(r => {
      this.user = this.data.getUser();
      this.languages = this.data.getLanguages();
      if (this.user.role !== "TEACHER") {
        this.router.navigate(['/']);
      }
      if (this.user.username == null) {
        this.router.navigate(['/login']);
      }

    }).then(r => {
        this.spinner.hide();
        this.isPageLoading = false;
      }
    );
  }

  onCreate() {
    this.error = '';
    if (this.courseName == '' || this.courseShortDescription == '' || this.courseDescription == '' || this.courseLanguage == '' || this.courseCategory == '' || this.courseUrl == '') {
      this.error = "Please fill required fields";
      return;
    } else {
      this.axiosService.requestWithHeaderAuth(
        "POST",
        "/course/create?username=" + this.user.username,
        {
          "name": this.courseName,
          "description": this.courseDescription,
          "shortDescription": this.courseShortDescription,
          "language": this.courseLanguage,
          "category": this.courseCategory,
          "isPublic": this.coursePolicy,
          "url": this.courseUrl
        },
        this.user.secure_TOKEN
      ).catch((error) => {
        if (error.response.data.message == "Invalid SECURE_TOKEN" && !this.isUpdate) {
          this.data.updateUser().then(() => {
            this.onCreate();
            this.isUpdate = true;
          })
        } else {
          this.error = error.response.data.message;
        }
      })
        .then((response) => {
            if (response) {
              window.location.href = "/course/" + this.courseUrl + "/edit";
            }
          }
        )
    }
  }
}
