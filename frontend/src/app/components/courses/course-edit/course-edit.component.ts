import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {AxiosService} from "../../../axios.service";
import Editor from 'ckeditor5-custom-build';
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import {DataService} from "../../../data.service";
import {NgxSpinnerService} from "ngx-spinner";
import {MatDialog} from '@angular/material/dialog';
import {AddSectionComponent} from "./add-section/add-section.component";


export interface DialogData {
  sectionName: string;
  name: string;
}

@Component({
  selector: 'app-course-edit',
  templateUrl: './course-edit.component.html',
  styleUrls: ['./course-edit.component.css'],
})
export class CourseEditComponent implements OnInit {
  Editor = Editor;
  faPlus = faPlus;
  courseUrl = '';
  user = JSON.parse(localStorage.getItem('user') || '{}');
  course: any = {};
  languages: any;
  isLoading: boolean = true;
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
              private spinner: NgxSpinnerService,
              public dialog: MatDialog) {

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
    //TODO https://v6.material.angular.io/components/dialog/overview Connect this and get section name from modal window
    const dialogRef = this.dialog.open(AddSectionComponent, {
      width: '300px',
      data: {name: "Add section name"}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed ' + result);
      if (result !== undefined && result !== "" && result.replace(/\s/g, '') !== "") {
        this.sections.push({name: result, videos: []});
      }
    });
  }

  changeIsEdit(isEdit: boolean) {
    this.isEdit = !isEdit;
  }

  showSpinner() {
    this.spinner.show().then(r => console.log("Loading..."));
    this.isLoading = true;
  }

  hideSpinner() {
    this.spinner.hide();
    this.isLoading = false;
  }

  addVideoToSection(name: string) {
    //TODO https://v6.material.angular.io/components/dialog/overview Connect this and get video name from modal window
    this.sections.forEach((section: any) => {
      // add video to section by name of section (section.name) but if section name is "Add section name" then print error
      if (section.name === name && section.name !== "Add section name ") {
        section.videos.push({name: "Add video name", url: ""});
      } else {
        // open modal window with error
        alert("Please change section name before add video");
      }
    });
  }

  changeCourseLanguage(language: string) {
    console.log("change lang: " + language);
    this.courseLanguage = language;
  }
}
