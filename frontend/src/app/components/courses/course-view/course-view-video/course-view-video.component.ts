import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DataService} from "../../../../data.service";
import {NgxSpinnerService} from "ngx-spinner";
import {AxiosService} from "../../../../axios.service";
import {Title} from "@angular/platform-browser";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-course-view-video',
  templateUrl: './course-view-video.component.html',
  styleUrls: ['./course-view-video.component.css']
})
export class CourseViewVideoComponent implements OnInit {

  isLoading: boolean = true;
  errorMessage: string = '';
  course: any = {};
  courseUrl = '';
  courseVideoUrl = '';
  selectedFileUrl: any = '';
  user: any = JSON.parse(localStorage.getItem("user") || '{}');

  internalization: any;
  isJoined: boolean = false;
  isSidebarOpen: boolean = true;

  constructor(private route: ActivatedRoute,
              private data: DataService,
              private router: Router,
              private http: HttpClient,
              private titleService: Title,
              private axiosService: AxiosService,
              private spinner: NgxSpinnerService,) {
  }

  ngOnInit(): void {
    this.spinner.show().then(r => {
      this.internalization = this.data.getInternalization();
      this.getCourseUrl();
      this.getCourseVideoUrl();

//get video from path


      this.getCourseData(this.courseUrl)
        .catch((error) => {
          if (error) {
            this.spinner.hide();
            this.errorMessage = error.response.data?.message || error.response.data;
          }
        });

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
        this.router.navigate(['/']);
      }
    }).then((response) => {
      if (response) {
        this.course = null;
        this.course = response.data;
        this.titleService.setTitle(response.data.name + " | Course");
        this.checkIsUserJoinedCourse()
        this.getVideo();
      }
    });
  }

  async getVideo() {
    await this.axiosService.getVideo(
      this.user.secure_TOKEN,
      "/course/" + this.courseUrl + "/video/" + this.courseVideoUrl + "?username=" + this.user.username
    ).then((response) => {
      this.selectedFileUrl = URL.createObjectURL(response.data);

      this.spinner.hide();
      this.isLoading = true;
    }).catch((error) => {
      if (error) {
        this.spinner.hide();
        this.errorMessage = error.response.data?.message || error.response.data;
      }
    });

  }

  checkIsUserJoinedCourse() {
    if (this.user.coursesId.includes(this.course.id)) {
      this.isJoined = true;
    } else {
      this.router.navigate(['/course/' + this.courseUrl]);
    }
  }

  private getCourseUrl(): any {
    this.route.params.subscribe(params => {
      this.courseUrl = params['name'];
    });
  }

  private getCourseVideoUrl(): any {
    this.route.params.subscribe(params => {
      this.courseVideoUrl = params['url'];
    });
  }

  handleSidebarChange(isOpen: boolean
  ) {
    this.isSidebarOpen = isOpen;
  }
}
