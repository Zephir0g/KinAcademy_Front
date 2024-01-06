import {Component, OnInit} from '@angular/core';
import {faGlobe} from '@fortawesome/free-solid-svg-icons';
import {AxiosService} from "../../../axios.service";
import {DataService} from "../../../data.service";
import { AvatarModule } from 'primeng/avatar';
import { AvatarGroupModule } from 'primeng/avatargroup';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: any = {};
  internalization: any;
  faGlobe = faGlobe


  constructor(private data: DataService, private axiosService: AxiosService) {
  }

  ngOnInit(): void {
    this.user = this.data.getUser();
    this.internalization = this.data.getInternalization();
  }

  onLogout() {
    localStorage.clear();
  }


}
