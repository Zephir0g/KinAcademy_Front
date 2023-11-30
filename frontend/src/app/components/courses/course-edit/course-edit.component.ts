import {Component, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";


@Component({
  selector: 'app-course-edit',
  templateUrl: './course-edit.component.html',
  styleUrls: ['./course-edit.component.css'],
})
export class CourseEditComponent implements OnInit{
  isEdit: boolean = false;
  courseUrl = '';


  constructor(public dialog: MatDialog, private route: ActivatedRoute, private titleService: Title) {

  }

  changeIsEdit(isEdit: boolean) {
    this.isEdit = !isEdit;
  }

  ngOnInit(): void {
    this.getCourseUrl();
  }

  getCourseUrl() {
    this.route.params.subscribe(params => {
      this.courseUrl = params['name']
    });
  }

}
