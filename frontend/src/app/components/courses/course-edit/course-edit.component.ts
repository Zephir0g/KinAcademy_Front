import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {AxiosService} from "../../../axios.service";
import Editor from 'ckeditor5-custom-build';
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import {DataService} from "../../../data.service";

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
              private data: DataService) {

  }

  ngOnInit(): void {
    this.getCourseUrl();

    if (localStorage.getItem("course-" + this.courseUrl) === null) {

      //else get course from server
      this.getCourseData(this.courseUrl);
    } else {
      this.course = JSON.parse(localStorage.getItem("course-" + this.courseUrl) || '{}');
    }

    //check if user is author of course
    if (!this.userIsAuthor()) {
      window.location.href = '/course/' + this.courseUrl;
    }
    this.languages = this.data.getLanguages();

    //Get data from course
    this.courseImage = this.course.imageUrl || this.imageNotFound;
    this.courseDescription = this.course.description;
    this.courseName = this.course.name;
    this.courseLanguage = this.course.language;
    this.courseCategory = this.course.category;
    this.sections = this.course.sections;
    console.log(this.course);
  }

  userIsAuthor(): boolean {
    this.isAuthor = this.user.id === this.course.authorId;
    return this.isAuthor;
  }

  getCourseData(url: string) {
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

  getCourseUrl() {
    this.route.params.subscribe(params => {
      this.courseUrl = params['name']
    });
  }

  addSection() {
    this.sections.put({name: "", description: "", videos: []});
  }

  changeIsEdit(isEdit
                 :
                 boolean
  ) {
    this.isEdit = !isEdit;
  }
}
