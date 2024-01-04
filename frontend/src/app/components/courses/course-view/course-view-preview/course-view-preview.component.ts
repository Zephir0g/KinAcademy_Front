import {Component, Input} from '@angular/core';
import {environment} from "../../../../../../environments/environment";
import Editor from "ckeditor5-custom-build";
import {faGlobe} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-course-view-preview',
  templateUrl: './course-view-preview.component.html',
  styleUrls: ['./course-view-preview.component.css']
})
export class CourseViewPreviewComponent {

  @Input() internalization: any
  @Input() course: any
  @Input() authorUsername: any

  Editor = Editor
  user: any = JSON.parse(localStorage.getItem("user") || '{}');
  faGlobe = faGlobe

  getLastUpdateDate() {
    const date = new Date(this.course.lastUpdateDate);
    return date.toLocaleDateString();
  }


  protected readonly environment = environment;
}
