import {Component, Input, OnInit} from '@angular/core';
import {faMagnifyingGlass, faXmark} from '@fortawesome/free-solid-svg-icons';
import {AxiosService} from "../../axios.service";
import {DataService} from "../../data.service";

@Component({
  selector: 'app-menu',
  templateUrl: '../menu/menu.component.html',
  styleUrls: ['../menu/menu.component.css']
})
export class MenuComponent implements OnInit {
  @Input() pageTitle!: string;
  @Input() logoSrc!: string;

  faMagnifyingGlass = faMagnifyingGlass
  faXmark = faXmark

  user: any = {};
  showFullscreenSearch: boolean = false;

  constructor(private axiosService: AxiosService, private data: DataService) {

  }


  toggleFullscreenSearch(state: boolean) {
    this.showFullscreenSearch = state;
  }

  ngOnInit(): void {
    this.user = this.data.getUser();
  }
}
