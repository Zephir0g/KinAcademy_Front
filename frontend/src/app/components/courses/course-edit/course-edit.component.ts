import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {AxiosService} from "../../../axios.service";

@Component({
  selector: 'app-course-edit',
  templateUrl: './course-edit.component.html',
  styleUrls: ['./course-edit.component.css']
})
export class CourseEditComponent implements OnInit{
  courseUrl = '';
  user = JSON.parse(localStorage.getItem('user') || '{}');
  course: any = {};
  isAuthor = false;


  constructor(private route: ActivatedRoute,
              private titleService: Title,
              private axiosService: AxiosService) {

  }

  ngOnInit(): void {
    this.getCourseUrl();

    if (localStorage.getItem("course-" + this.courseUrl) == null) {
      //else get course from server
      this.getCourseData(this.courseUrl);
    } else {
      this.course = JSON.parse(localStorage.getItem("course-" + this.courseUrl) || '{}');
    }

    //check if user is author of course
    this.userIsAuthor();
    if (!this.isAuthor) {
      //if not author, redirect to course view
      //window.location.href = '/course/' + this.courseUrl;
    }
  }

  userIsAuthor() {
    this.isAuthor = this.user.id === this.course.authorId;
  }

  getCourseData(url:string){
    this.axiosService.request(
      "GET",
      "/course/" + url,
      null
    ).catch((error) => {
      console.log(error.response.data.message);
    })
      .then((response) => {
          if (response) {
            console.log(response.data)
            this.titleService.setTitle(response.data.name + " | Course");
            localStorage.setItem("course-" + response.data.url, JSON.stringify(response.data));
            this.course = JSON.parse(localStorage.getItem('course-' + response.data.url) || '{}');
          }
        }

      )
  }

  getCourseUrl(){
    this.route.params.subscribe(params => {
      this.courseUrl = params['name']
    });
  }

}
