import {Component, EventEmitter, Input, Output} from '@angular/core';
import {faArrowRight, faXmark} from '@fortawesome/free-solid-svg-icons';
import {Router} from "@angular/router";

@Component({
  selector: 'app-course-video-panel',
  templateUrl: './course-video-panel.component.html',
  styleUrls: ['./course-video-panel.component.css']
})
export class CourseVideoPanelComponent {
  @Output() sidebarOpenChange = new EventEmitter<boolean>();
  @Input() course: any;
  isSidebarOpen: boolean = true;
  faXmark = faXmark;


  constructor(private router: Router) {
  }

  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
    this.sidebarOpenChange.emit(this.isSidebarOpen);
  }

  openVideo(url: string) {
    const videoUrl = '/course/' + this.course.url + '/video/' + url;
    window.location.href = videoUrl;
  }

  protected readonly faArrowRight = faArrowRight;
  protected readonly JSON = JSON;
}
