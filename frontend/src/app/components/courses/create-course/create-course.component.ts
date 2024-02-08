import {Component, OnInit} from '@angular/core';
import Editor from 'ckeditor5-custom-build';
import {AxiosService} from "../../../axios.service";
import {DataService} from "../../../data.service";
import {Title} from "@angular/platform-browser";
import {NgxSpinnerService} from "ngx-spinner";
import {Router} from "@angular/router";
import {MessageService, PrimeNGConfig} from "primeng/api";

@Component({
  selector: 'app-create-course',
  templateUrl: './create-course.component.html',
  styleUrls: ['./create-course.component.css'],
  providers: [MessageService]
})
export class CreateCourseComponent implements OnInit {
  isPageLoading = false;
  Editor = Editor;
  user: any;
  languages: any;
  courseDescription: String = '';
  courseShortDescription: String = '';
  courseName: String = '';
  courseLanguage: String = '';
  courseCategory: String = '';
  coursePolicy: boolean = true;
  courseUrl: String = '';
  error: String = '';
  categories: any;


  constructor(private axiosService: AxiosService,
              private data: DataService,
              private titleService: Title,
              private spinner: NgxSpinnerService,
              private messageService: MessageService,
              private primengConfig: PrimeNGConfig,
              private router: Router) {
    this.titleService.setTitle("Create course");
  }

  ngOnInit(): void {
    this.isPageLoading = true;
    this.primengConfig.ripple = true;
    this.categories = this.data.getCategories();
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

  getEmptyItems(categoryList: any): Array<any> {
    let emptyLabels = [];
    for (const category of categoryList) {
      if (category.items.length === 0) {
        emptyLabels.push(category);
      }
      if (category.items.length > 0) {
        emptyLabels = emptyLabels.concat(this.getEmptyItems(category.items));
      }
    }
    return emptyLabels.sort();
  }

  onCreate() {
    this.error = '';
    if (this.courseName == '' || this.courseShortDescription == '' || this.courseDescription == '' || this.courseLanguage == '' || this.courseCategory == '' || this.courseUrl == '') {
      this.messageService.add({severity: 'error', summary: 'Error', detail: 'Please fill required fields'});
      return;
    } else {
      this.courseUrl = this.courseUrl.replace(/\s+/g, '-').toLowerCase();
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
        this.error = error.response.data.message;
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
