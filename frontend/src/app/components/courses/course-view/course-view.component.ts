import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {AxiosService} from "../../../axios.service";

@Component({
  selector: 'app-course-view',
  templateUrl: './course-view.component.html',
  styleUrls: ['./course-view.component.css']
})
export class CourseViewComponent implements OnInit{
  @Input() pageTitle!: string;
  courseUrl = '';
  course: any = {};

  constructor(private route: ActivatedRoute,
              private titleService:Title,
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
  }

  getCourseUrl(){
    this.route.params.subscribe(params => {
      this.courseUrl = params['name']
    });
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
          localStorage.setItem("course-" + response.data.url, JSON.stringify(response.data));
          this.titleService.setTitle(response.data.name + " | Course");
        }
      }

    )
  }
}
