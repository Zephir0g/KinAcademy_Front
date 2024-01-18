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

   /**
   * Changes the current language of the application and updates the data accordingly.
   * @param {string} language - The language code to switch the application to.
   */
  changeLanguage(language: string) {

    // Requests the server to fetch the categories in the selected language.
    this.data.getCategoriesFromServer(language);

    // Requests the server to fetch the internationalization files for the new language.
    // Returns a promise that resolves after the request is complete.
    this.data.getInternalizationFromServerWithLanguage(language).then(() => {
      // After successfully obtaining the internationalization data, the user's language
      // information is updated.
      this.updateUser(language).then(() => {
        // Reload the page to apply the language changes.
        window.location.reload();
      });
    });

    // Updates the user's language preference on the server to persist the choice.
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
