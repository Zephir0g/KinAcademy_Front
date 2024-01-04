import {Component, Input} from '@angular/core';
import {DataService} from "../../../../data.service";
import {NgxSpinnerService} from "ngx-spinner";
import {AxiosService} from "../../../../axios.service";

@Component({
  selector: 'app-course-view-outlook',
  templateUrl: './course-view-outlook.component.html',
  styleUrls: ['./course-view-outlook.component.css']
})
export class CourseViewOutlookComponent {

  @Input() internalization: any
  @Input() course: any

  constructor(private data: DataService,
              private spinner: NgxSpinnerService,
              private axiosService: AxiosService) {
  }
}
