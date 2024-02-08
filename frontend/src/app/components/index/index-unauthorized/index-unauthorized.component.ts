import {Component, OnInit} from '@angular/core';
import {AxiosService} from "../../../axios.service";

@Component({
  selector: 'app-index-unauthorized',
  templateUrl: './index-unauthorized.component.html',
  styleUrls: ['./index-unauthorized.component.css']
})
export class IndexUnauthorizedComponent implements OnInit {
  popularCourses: any = {};

  constructor(private axios: AxiosService) {
  }

  ngOnInit(): void {
    this.getPopularCourses();
  }

  private getPopularCourses() {
    this.axios.request(
      "GET",
      "/components/popular",
      null
    ).then((response: any) => {
      sessionStorage.setItem("popularCourses", JSON.stringify(response.data));
      this.popularCourses = JSON.parse(sessionStorage.getItem("popularCourses") || '{}');
      console.log("popular", this.popularCourses);
    })
  }

  protected readonly JSON = JSON;
}
