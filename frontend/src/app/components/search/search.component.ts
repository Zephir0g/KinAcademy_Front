import {Component, OnInit} from '@angular/core';
import {faGlobe} from "@fortawesome/free-solid-svg-icons";
import {NgxSpinnerService} from "ngx-spinner";
import {DataService} from "../../data.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  internalization = JSON.parse(localStorage.getItem('internalization') || '{}')
  user = JSON.parse(localStorage.getItem('user') || '{}')
  languages = JSON.parse(localStorage.getItem('languages') || '{}')

  isLoading: boolean = true;
  searchName: string = '';
  searchCategory: string = '';

  courses: any;

  protected readonly faGlobe = faGlobe;

  constructor(private spinner: NgxSpinnerService,
              private route: ActivatedRoute,
              private data: DataService) {
  }

  ngOnInit(): void {
    this.spinner.show().then(r => {
      this.getParams();
      this.data.search(this.searchName, this.searchCategory).then((response: any) => {
        if (response) {
          this.courses = response.data;
          this.spinner.hide();
          this.isLoading = false;
        }
      })
    });

  }

  getParams() {
    this.searchName = this.route.snapshot.queryParamMap.get('name') || '';
    this.searchCategory = this.route.snapshot.queryParamMap.get('category') || '';
  }

  getLanguageName(course: any): string {
    const selectedLanguage = this.languages.find((language: { code: any; }) => language.code === course.language);
    return selectedLanguage.label;
  }

  openCourse(url: string) {
    window.location.href = "/course/" + url;
  }

  protected readonly JSON = JSON;
}
