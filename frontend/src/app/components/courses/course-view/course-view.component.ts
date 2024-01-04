import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {AxiosService} from "../../../axios.service";
import {DataService} from "../../../data.service";
import {environment} from "../../../../../environments/environment";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  selector: 'app-course-view',
  templateUrl: './course-view.component.html',
  styleUrls: ['./course-view.component.css']
})
export class CourseViewComponent implements OnInit {
  @Input() pageTitle!: string;

  courseUrl = '';
  course: any = {};
  authorUsername: string = '';
  lastUpdateDate: string = '';
  internalization: any;
  errorMessage: string = '';
  user: any = JSON.parse(localStorage.getItem("user") || '{}');


  isLoading: boolean = true;
  isJoined: boolean = false;

  constructor(private route: ActivatedRoute,
              private titleService: Title,
              private data: DataService,
              private spinner: NgxSpinnerService,
              private axiosService: AxiosService) {

  }

//TODO check is user joined course
  async ngOnInit() {
    this.spinner.show().then(r => {
      this.getCourseUrl();
      this.internalization = this.data.getInternalization();

      this.getCourseData(this.courseUrl)
        .catch((error) => {
          if (error) {
            this.spinner.hide();
            this.errorMessage = error.response.data?.message || error.response.data;
          }
        });

    });
  }

  getCourseUrl() {
    this.route.params.subscribe(params => {
      this.courseUrl = params['name']
    });
  }

  async getCourseData(url: string) {

    this.axiosService.requestWithHeaderAuth(
      "GET",
      "/course/" + url + "?username=" + this.user.username,
      null,
      this.user.secure_TOKEN
    ).catch((error) => {
      if (error) {
        this.errorMessage = error.response.data?.message || error.response.data;
      }
    }).then((response) => {
      if (response) {
        this.course = null;
        this.course = response.data;
        this.titleService.setTitle(response.data.name + " | Course");
        this.checkIsUserJoinedCourse()
        this.getAuthorName().then((response) => {
          this.spinner.hide();
          this.isLoading = false;
        });
      }
    });
  }

  async getAuthorName() {
    this.axiosService.request(
      "GET",
      "/components/authorname?authorUsername=" + this.course.authorUsername,
      null
    ).then((response) => {
      this.authorUsername = response.data;
    }).catch((error) => {
      console.log(error.response.data.message);
    });
  }

  checkIsUserJoinedCourse() {
    if (this.user.coursesId.includes(this.course.id)) {
      this.isJoined = true;
    }
  }

  protected readonly JSON = JSON;
  protected readonly environment = environment;
}
