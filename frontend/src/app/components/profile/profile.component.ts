import { Component } from '@angular/core';
import { faGlobe } from '@fortawesome/free-solid-svg-icons';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
  user = JSON.parse(localStorage.getItem('user') || '{}');
  languages = JSON.parse(localStorage.getItem('languages') || '{}');
  faGlobe = faGlobe

  onLogout() {
    localStorage.removeItem('user');
    localStorage.removeItem('languages');
  }


}
