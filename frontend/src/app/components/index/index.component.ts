import {Component, OnInit} from '@angular/core';
import {AxiosService} from "../../axios.service";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {
  title = 'frontend';
  user = JSON.parse(localStorage.getItem("user") || "{}");
  languages = JSON.parse(localStorage.getItem("languages") || "{}");

  isLogesIn: boolean = false;

  constructor(private axiosService: AxiosService) {
  }

  ngOnInit(): void {
    // console.log("LoginComponent.ngOnInit()");
    /*this.axiosService.requestWithHeaderLang(
      "GET",
      "/components/languages",
      {},
      this.user.lang

    )*/

    this.checkIsTokenValid();
    this.getLanguages();
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
         this.updateUser();
        })
    }
  }


  updateUser() {
    this.axiosService.request(
      "POST",
      "/login",
      {
        "login": this.user.login,
        "password": "qwe"
      },
    ).then((response) => {
      if (response) {
        if (!this.isLogesIn) {
          console.log("Reloading");
          this.isLogesIn = true;
          localStorage.removeItem("user");
          localStorage.setItem("user", JSON.stringify(response.data));
          this.user = JSON.parse(localStorage.getItem("user") || "{}");
          this.checkIsTokenValid();
        }
      }
    }).catch((error) => {
      console.log("Error while updating user")
      console.log(error.response.data);
    })
  }

  getLanguages() {
    if (Object.keys(this.languages).length === 0) {
      this.axiosService.requestWithHeaderLang(
        "GET",
        "/components/languages",
        {},
        this.user.language || "English"
      ).then((response) => {
        // this.userLanguages = response.data;
        localStorage.setItem("languages", JSON.stringify(response.data));
        this.languages = JSON.parse(localStorage.getItem("languages") || "{}");
      }).catch((error) => {
        console.log(error.response.data.message);
      })
    }
  }
}
