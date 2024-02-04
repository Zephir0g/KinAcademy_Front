import {Component, OnInit} from '@angular/core';
import {DataService} from "../../data.service";
import {NgxSpinnerService} from "ngx-spinner";
import {faGlobe} from "@fortawesome/free-solid-svg-icons";
import {AxiosService} from "../../axios.service";
import {Object} from "core-js";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {
  title = 'Kin Academy';
  user: any;
  languages: any;
  categories: any;
  internalization: any;
  userCourses: any = [];

  isPageLoading: boolean = false;

  constructor(private data: DataService,
              private axiosService: AxiosService,
              private spinner: NgxSpinnerService) {
  }

  async ngOnInit() {
    this.isPageLoading = true;
    this.spinner.show().then(r => {
      this.getData().then(() => {
        if (this.user.username !== undefined && this.user.username !== '') {
          this.userCourses = JSON.parse(localStorage.getItem('userCourses') || '{}');
          console.log("user course", this.userCourses);
          this.userCourses.forEach((course: any) => {
            this.getAuthorName(course).then((response: any) => {
              if (response) {
                course['authorName'] = response.data;
              }
            });
          })
        }
        this.spinner.hide();
        this.isPageLoading = false;
      });
    });
  }

  async getData() {
    try {
      await Promise.all([
        this.getUser(),
        this.getInternalization(),
        //this.data.checkIsTokenValid(),
        this.getLanguage(),
        this.getCategories(),
        this.getListOfUserCoursesFromServer(this.user)
      ]);
    } catch (error) {
      console.error("Error occurred:", error);
    }
  }

  async getUser() {
    this.user = JSON.parse(localStorage.getItem('user') || '{}');
    return this.user;
  }

  async getInternalization() {
    this.internalization = JSON.parse(localStorage.getItem('internalization') || '{}');
    return this.internalization;
  }

  async getLanguage() {
    this.languages = this.data.getLanguages();
    return this.languages;
  }

  async getCategories() {
    this.categories = this.data.getCategoriesFromServer(this.user.language || "English");
    return this.categories;
  }

  async getListOfUserCoursesFromServer(user: any) {
    if (this.user !== '{}') {
      this.userCourses = this.data.getListOfUserCoursesFromServer();
      return this.userCourses;
    }
  }

  getAuthorName(course: any) {
    return this.axiosService.request(
      "GET",
      "/components/authorname?authorUsername=" + course.authorUsername,
      null
    );
  }

  getLanguageName(course: any): string {
    const selectedLanguage = this.languages.find((language: { code: any; }) => language.code === course.language);
    return selectedLanguage.label;
  }

  openCourse(url: string) {
    window.location.href = "/course/" + url;
  }


  protected readonly JSON = JSON;
  protected readonly faGlobe = faGlobe;
  protected readonly Object = Object;
}
