import {Component, EventEmitter, Input, Output} from '@angular/core';
import {faArrowRight, faXmark} from '@fortawesome/free-solid-svg-icons';
import {DataService} from "../../../../../data.service";

@Component({
  selector: 'app-course-video-panel',
  templateUrl: './course-video-panel.component.html',
  styleUrls: ['./course-video-panel.component.css']
})
export class CourseVideoPanelComponent {
  @Output() sidebarOpenChange = new EventEmitter<boolean>();
  @Input() course: any;
  user = JSON.parse(localStorage.getItem('user') || '{}');
  isSidebarOpen: boolean = true;
  faXmark = faXmark;


  constructor(private data: DataService) {
  }

  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
    this.sidebarOpenChange.emit(this.isSidebarOpen);
  }

  openVideo(url: string) {
    const videoUrl = '/course/' + this.course.url + '/video/' + url;
    window.location.href = videoUrl;
  }

  onCheckboxChange(video: any) {
    let sectionIndex = -1;
    let videoIndex = -1;

    // Find the index of the video in course.sections.videos
    sectionIndex = this.course.sections.findIndex((section: { id: any; }) => section.id === video.sectionId);
    if (sectionIndex !== -1) {
      videoIndex = this.course.sections[sectionIndex].videos.findIndex((v: {
        urlToVideo: any;
      }) => v.urlToVideo === video.urlToVideo);
      if (videoIndex !== -1) {
        // Toggle the isWatched property of the found video
        this.course.sections[sectionIndex].videos[videoIndex].isWatched = !this.course.sections[sectionIndex].videos[videoIndex].isWatched;
      }
    }


    this.data.changeStatusWatchedVideo(this.user, this.course.url, video.urlToVideo).then((response: any) => {
      if (response) {
      }
    }).catch((error) => {
      if (error) {
        console.log(error.response.data);
        if (sectionIndex !== -1 && videoIndex !== -1) {
          this.course.sections[sectionIndex].videos[videoIndex].isWatched = !this.course.sections[sectionIndex].videos[videoIndex].isWatched;
        }
      }
    })
  }

  protected readonly faArrowRight = faArrowRight;
  protected readonly JSON = JSON;
}
