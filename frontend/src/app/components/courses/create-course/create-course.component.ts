import {Component, OnInit} from '@angular/core';
import Editor from 'ckeditor5-custom-build';
import {AxiosService} from "../../../axios.service";

@Component({
  selector: 'app-create-course',
  templateUrl: './create-course.component.html',
  styleUrls: ['./create-course.component.css']
})
export class CreateCourseComponent implements OnInit{
  public Editor = Editor;
  user = JSON.parse(localStorage.getItem('user') || '{}');
  courseDescription: String = '';
  courseName: String = '';
  courseLanguage: String = '';
  courseCategory: String = '';
  coursePolicy: boolean = true;
  courseUrl: String = '';
  error: String = '';


  constructor(private axiosService: AxiosService) {
  }

  ngOnInit(): void {
    if (this.user.id == null) {
      window.location.href = "/login";
    }
    if(!this.user.roles.includes("TEACHER")){
      window.location.href = "/";
    }
  }

  onCreate() {
    this.error = '';
    if (this.courseName == '' || this.courseDescription == '' || this.courseLanguage == '' || this.courseCategory == '' || this.courseUrl == '') {
      this.error = "Please fill required fields";
      return;
    } else{
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
        this.error = error.response.data.message;
      })
        .then((response) => {
          if (response) {
            console.log(response.data)
             window.location.href = "/course/" + this.courseUrl + "/edit";
          }
        }
      )
    }
  }


}
