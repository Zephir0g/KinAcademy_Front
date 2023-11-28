import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {AxiosService} from "../../../axios.service";
import Editor from 'ckeditor5-custom-build';
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import {DataService} from "../../../data.service";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  selector: 'app-course-edit',
  templateUrl: './course-edit.component.html',
  styleUrls: ['./course-edit.component.css']
})
export class CourseEditComponent implements OnInit {
  Editor = Editor;
  faPlus = faPlus;
  courseUrl = '';
  user = JSON.parse(localStorage.getItem('user') || '{}');
  course: any = {};
  languages: any;
  isAuthor = false;
  isEdit: boolean = false;

  imageNotFound: string = "https://www.salonlfc.com/wp-content/uploads/2018/01/image-not-found-scaled-1150x647.png";

  courseImage: string = '';
  courseDescription: String = '';
  courseName: String = '';
  courseLanguage: String = '';
  courseCategory: String = '';
  sections: any;


  constructor(private route: ActivatedRoute,
              private titleService: Title,
              private axiosService: AxiosService,
              private data: DataService,
              private spinner: NgxSpinnerService) {

  }

  ngOnInit(): void {
    this.getCourseUrl();
    this.showSpinner();
    this.loadCourseData();
  }

  loadCourseData() {
    if (localStorage.getItem("course-" + this.courseUrl) === null) {
      this.fetchCourseDataFromServer();
    } else {
      this.course = JSON.parse(localStorage.getItem("course-" + this.courseUrl) || '{}');
      this.hideSpinner();
      this.loadCourseDetails();
    }
  }

  fetchCourseDataFromServer() {
    this.getCourseData(this.courseUrl)
      .then((response) => {
        if (response) {
          this.titleService.setTitle(response.data.name + " | Course");
          localStorage.setItem("course-" + response.data.url, JSON.stringify(response.data));
          this.course = response.data;
          this.hideSpinner();
          this.loadCourseDetails();
        }
      })
      .catch((error) => {
        console.log(error.response.data.message);
        this.hideSpinner();
      });
  }

  loadCourseDetails() {
    if (!this.userIsAuthor()) {
      window.location.href = '/course/' + this.courseUrl;
    }
    this.languages = this.data.getLanguages();
    this.courseImage = this.course.imageUrl || this.imageNotFound;
    this.courseDescription = this.course.description;
    this.courseName = this.course.name;
    this.courseLanguage = this.course.language;
    this.courseCategory = this.course.category;
    this.sections = this.course.sections;
  }

  userIsAuthor(): boolean {
    return this.user.id === this.course.authorId;
  }

  getCourseData(url: string): Promise<any> {
    return this.axiosService.request("GET", "/course/" + url, null);
  }

  getCourseUrl() {
    this.route.params.subscribe(params => {
      this.courseUrl = params['name']
    });
  }

  addSection() {
    this.sections.push({name: "", description: "", videos: []});
  }

  changeIsEdit(isEdit: boolean) {
    this.isEdit = !isEdit;
  }

  showSpinner() {
    this.spinner.show().then(r => console.log("Loading..."));
  }

  hideSpinner() {
    this.spinner.hide();
  }

}
