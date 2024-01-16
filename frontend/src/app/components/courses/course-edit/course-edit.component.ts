import {Component, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {ActivatedRoute, Router} from "@angular/router";
import {DataService} from "../../../data.service";
import {AxiosService} from "../../../axios.service";
import {NgxSpinnerService} from "ngx-spinner";


@Component({
  selector: 'app-course-edit',
  templateUrl: './course-edit.component.html',
  styleUrls: ['./course-edit.component.css'],
})
export class CourseEditComponent implements OnInit {
  isEdit: boolean = false;
  courseUrl = '';
  user = JSON.parse(localStorage.getItem('user') || '{}');
  isLoading: boolean = true;

  course: any = {};
  categories: any = {};


  constructor(public dialog: MatDialog,
              private data: DataService,
              private route: ActivatedRoute,
              private router: Router,
              private spinner: NgxSpinnerService,
              private axiosService: AxiosService,) {

  }

  changeIsEdit(isEdit: boolean) {
    this.isEdit = !isEdit;
  }

  ngOnInit(): void {
    this.spinner.show();
    this.getCourseUrl();
    this.categories = this.data.getCategories();

    this.data.getCourseData(this.courseUrl).then((response: any) => {
      this.course = response.data;
      if (!this.userIsAuthor()) {
        this.router.navigate(['/course/' + this.courseUrl]);
      }
      this.isLoading = false;
      this.spinner.hide();
    }).catch((error) => {
      console.log(error);
    });


  }


  userIsAuthor(): boolean {
    return this.user.username === this.course.authorUsername;
  }

  getCourseUrl() {
    this.route.params.subscribe(params => {
      this.courseUrl = params['name']
    });
  }
}
