import {Component, Input} from '@angular/core';
import {environment} from "../../../../../../environments/environment";
import Editor from "ckeditor5-custom-build";
import {faGlobe} from "@fortawesome/free-solid-svg-icons";
import {DataService} from "../../../../data.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-course-view-preview',
  templateUrl: './course-view-preview.component.html',
  styleUrls: ['./course-view-preview.component.css']
})
export class CourseViewPreviewComponent {

  @Input() internalization: any
  @Input() course: any
  @Input() authorUsername: any


  constructor(private data: DataService,
              private router: Router) {
  }

  Editor = Editor
  user: any = JSON.parse(localStorage.getItem("user") || '{}');
  faGlobe = faGlobe

  getLastUpdateDate() {
    const date = new Date(this.course.lastUpdateDate);
    return date.toLocaleDateString();
  }

  async joinCourse() {

    const response = await this.data.joinCourse(this.course.url)
    if (response.status === 200) {
      await this.data.updateUser()
      this.data.updateUser().then(() => {
        window.location.reload();
      });
    } else {
      //TODO show error message
      if (response.data === "User already joined course") {
        window.location.reload();
      } else {
        console.log(response.data)
      }
    }
  }


  protected readonly environment = environment;
  protected readonly JSON = JSON;
}
