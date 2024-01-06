import {Component, Input} from '@angular/core';
import {DataService} from "../../../../data.service";
import {NgxSpinnerService} from "ngx-spinner";
import {AxiosService} from "../../../../axios.service";
import {faArrowRight, faGlobe} from "@fortawesome/free-solid-svg-icons";
import {environment} from "../../../../../../environments/environment";
import Editor from "ckeditor5-custom-build";
import {Router} from "@angular/router";

@Component({
  selector: 'app-course-view-outlook',
  templateUrl: './course-view-outlook.component.html',
  styleUrls: ['./course-view-outlook.component.css']
})
export class CourseViewOutlookComponent {

  @Input() internalization: any
  @Input() course: any
  @Input() authorUsername: any
  faArrowRight = faArrowRight;


  Editor = Editor;

  constructor(private data: DataService,
              private router: Router,
              private spinner: NgxSpinnerService,
              private axiosService: AxiosService) {
  }

  getLastUpdateDate() {
    const date = new Date(this.course.lastUpdateDate);
    return date.toLocaleDateString();
  }

  openVideo(url: string) {
    this.router.navigate(['/course/' + this.course.name + '/video/' + url]);
  }

  protected readonly faGlobe = faGlobe;
  protected readonly environment = environment;
  protected readonly JSON = JSON;


}
