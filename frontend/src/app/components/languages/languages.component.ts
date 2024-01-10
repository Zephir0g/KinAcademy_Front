import {Component, OnInit} from '@angular/core';
import {AxiosService} from "../../axios.service";
import {DataService} from "../../data.service";

@Component({
  selector: 'app-languages',
  templateUrl: './languages.component.html',
  styleUrls: ['./languages.component.css']
})
export class LanguagesComponent implements OnInit {

  languages: any;
  user: any;

  constructor(private axiosService: AxiosService, private data: DataService) {
  }

  ngOnInit(): void {
    this.languages = this.data.getLanguages();
    this.user = this.data.getUser();
  }

  changeLanguage(language: string) {

    this.data.getCategoriesFromServer(language);

    this.data.getInternalizationFromServerWithLanguage(language).then(() => {
      this.updateUser(language).then(() => {
        window.location.reload();

      })
    })
    this.updateUserOnServer(language);
  }

  async updateUser(language: string) {
    this.user.language = language;
    localStorage.removeItem("user");
    localStorage.setItem("user", JSON.stringify(this.user));
    this.user = JSON.parse(localStorage.getItem("user") || "{}");

  }

  updateUserOnServer(language: string) {
    this.axiosService.requestWithHeaderAuth(
      "POST",
      "/users/update",
      {
        "id": this.user.id,
        "firstName": this.user.firstName,
        "surname": this.user.surname,
        "login": this.user.login,
        "email": this.user.email,
        "password": this.user.password,
        "language": language,
        "roles": this.user.roles,
        "status": this.user.status,
        "courseId": this.user.courseId,
        "secure_TOKEN": this.user.secure_TOKEN
      },
      "Bearer " + this.user.secure_TOKEN
    ).then((response) => {
      if (response) {
        console.log("User updated");
      }
    }).catch((error) => {
      console.log(error.response.data.message);
      if (error.response.data.message === "Token is invalid") {
        this.data.updateUser();
        this.updateUserOnServer(language);
      }
    })
  }


}
