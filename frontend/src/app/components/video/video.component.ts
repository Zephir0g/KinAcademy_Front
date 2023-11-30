import {Component, HostListener, Input} from '@angular/core';
import {VgApiService, VgFullscreenApiService} from "@videogular/ngx-videogular/core";

@Component({
  selector: 'app-video',
  templateUrl: './video.component.html',
  styleUrls: ['./video.component.css']
})
export class VideoComponent {
  @Input() selectedFile!: string;
  api: VgApiService = new VgApiService()
  fullScreenApi: VgFullscreenApiService = new VgFullscreenApiService();


  @HostListener('document:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (this.api === undefined) return;

    switch (event.code) {
      case 'ArrowLeft':
        this.api.seekTime(this.api.currentTime - 5);
        break;
      case 'ArrowRight':
        this.api.seekTime(this.api.currentTime + 5);
        break;
      case 'ArrowUp':
        this.api.volume = this.api.volume + 0.1;
        break;
      case 'ArrowDown':
        this.api.volume = this.api.volume - 0.1;
        break;
      case 'Space':
        this.api.state === 'playing' ? this.api.pause() : this.api.play();
        break;
      case 'KeyF':
        this.fullScreenApi.toggleFullscreen();
        break;
    }
  }

  onPlayerReady(source: VgApiService) {
    this.api = source;
    this.fullScreenApi = source.fsAPI;
  }
}
