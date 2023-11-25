import {Component, Input} from '@angular/core';
import { faXmark, faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-menu',
  templateUrl: '../menu/menu.component.html',
  styleUrls: ['../menu/menu.component.css']
})
export class MenuComponent {
  @Input() pageTitle!: string;
  @Input() logoSrc!: string;

  faMagnifyingGlass= faMagnifyingGlass
  faXmark= faXmark

  user = JSON.parse(localStorage.getItem('user') || '{}');
  showFullscreenSearch: boolean = false;

  toggleFullscreenSearch(state: boolean) {
    this.showFullscreenSearch = state;
  }


}
