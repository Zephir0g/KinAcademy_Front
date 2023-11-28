import {Component, OnInit} from '@angular/core';
import {AxiosService} from "../../axios.service";
import {DataService} from "../../data.service";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {
  title = 'frontend';
  user: any;
  languages: any;
  internalization: any;

  isLogesIn: boolean = false;
  isDataLoaded: boolean = false;

  constructor(private axiosService: AxiosService, private data: DataService) {
  }

  ngOnInit(): void {
    this.checkIsDataLoaded();
  }

  checkIsDataLoaded(): void {

    while (!this.isDataLoaded) {
      this.getUser();
      this.getInternalization();
      this.checkIsTokenValid();
      if (this.languages === undefined && this.internalization === undefined) {
        this.isDataLoaded = false;
      } else {
        this.isDataLoaded = true;
      }
    }
  }

  getUser() {
    this.user = this.data.getUser()
  }

  getInternalization() {
    this.internalization = this.data.getInternalization()
  }

  // Check if the user is logged in and if the token is valid
  checkIsTokenValid() {
    if (Object.keys(this.user).length === 0) {
    } else {
      this.axiosService.requestWithHeaderAuth(
        "GET",
        "/is-secure-token-valid?userId=" + this.user.id,
        null,
        "Bearer " + this.user.secure_TOKEN
      ).then((response) => {
        if (response) {
          console.log("Token is valid");
        }
      })
        .catch((error) => {
          console.log(error.response.data.message);
         // this.updateUser();
          this.data.updateUser();
        })
    }
  }





}
