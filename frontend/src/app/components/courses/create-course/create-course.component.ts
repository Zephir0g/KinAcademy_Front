import {Component, OnInit} from '@angular/core';
import Editor from 'ckeditor5-custom-build';
import {AxiosService} from "../../../axios.service";
import {DataService} from "../../../data.service";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-create-course',
  templateUrl: './create-course.component.html',
  styleUrls: ['./create-course.component.css']
})
export class CreateCourseComponent implements OnInit {
  Editor = Editor;
  user: any;
  languages: any;
  isUpdate = false;
  courseDescription: String = '';
  courseName: String = '';
  courseLanguage: String = '';
  courseCategory: String = '';
  coursePolicy: boolean = true;
  courseUrl: String = '';
  error: String = '';


  constructor(private axiosService: AxiosService, private data: DataService, private titleService: Title) {
    this.titleService.setTitle("Create course");
  }

  ngOnInit(): void {
    this.user = this.data.getUser();
    this.languages = this.data.getLanguages();
    if (this.user.id == null) {
      window.location.href = "/login";
    }
    if (!this.user.roles.includes("TEACHER")) {
      window.location.href = "/";
    }
    /*console.log(this.languages)*/
  }

  onCreate() {
    this.error = '';
    if (this.courseName == '' || this.courseDescription == '' || this.courseLanguage == '' || this.courseCategory == '' || this.courseUrl == '') {
      this.error = "Please fill required fields";
      return;
    } else {
      this.axiosService.requestWithHeaderAuth(
        "POST",
        "/course/create?userId=" + this.user.id,
        {
          "name": this.courseName,
          "description": this.courseDescription,
          "language": this.courseLanguage,
          "category": this.courseCategory,
          "isPublic": this.coursePolicy,
          "url": this.courseUrl
        },
        "Bearer " + this.user.secure_TOKEN
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
