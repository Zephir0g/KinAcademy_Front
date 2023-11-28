import {Injectable} from '@angular/core';
import {AxiosService} from "./axios.service";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class DataService {
  public user: any;
  public internalization: any;
  public languages: any;

  constructor(private axiosService: AxiosService, private router: Router,) {
    /*this.getInternalizationFromServer();
    this.getLanguagesFromServer();*/
  }

  public getUser(): any {
    this.user = JSON.parse(localStorage.getItem('user') || '{}');
    return this.user;
  }

  public getInternalization(): any {
    this.internalization = JSON.parse(localStorage.getItem('internalization') || '{}');
    return this.internalization;
  }

  public getLanguages(): any {
    this.languages = JSON.parse(localStorage.getItem('languages') || '{}');
    return this.languages;
  }

  public getInternalizationFromServer() {
    this.user = JSON.parse(localStorage.getItem('user') || '{}');
    this.axiosService.requestWithHeaderLang(
      "GET",
      "/components/internationalization",
      {},
      this.user.language
    ).then((response) => {
      if (response) {
        localStorage.removeItem('internalization');
        localStorage.setItem("internalization", JSON.stringify(response.data));
        this.internalization = JSON.parse(localStorage.getItem('internalization') || '{}');
      }
    }).catch((error) => {
      console.log(error.response.data.message);
    })
  }

  public async getInternalizationFromServerWithLanguage(lang: any) {
    return this.axiosService.requestWithHeaderLang(
      "GET",
      "/components/internationalization",
      {},
      lang || "English"
    ).then((response) => {
      if (response) {
        localStorage.removeItem('internalization');
        localStorage.setItem("internalization", JSON.stringify(response.data));
        this.internalization = JSON.parse(localStorage.getItem('internalization') || '{}')
        return this.internalization;
      }
    }).catch((error) => {
      console.log(error.response.data.message);
      throw error;
    })
  }

  public async getLanguagesFromServer() {
    if (localStorage.getItem('languages') === null) {
      return this.axiosService.request(
        "GET",
        "/components/languages",
        {},
      ).then((response) => {
        if (response) {
          localStorage.removeItem('languages');
          localStorage.setItem("languages", JSON.stringify(response.data));
          this.languages = JSON.parse(localStorage.getItem('languages') || '{}');
          return this.languages;
        }
      }).catch((error) => {
        console.log(error.response.data.message);
      })
    }
  }

  async updateUser() {
    this.axiosService.request(
      "POST",
      "/login",
      {
        "login": this.user.login,
        "password": this.user.password
      },
    ).then((response) => {
      if (response) {
        localStorage.removeItem("user");
        localStorage.setItem("user", JSON.stringify(response.data));
        this.user = JSON.parse(localStorage.getItem("user") || "{}");
      }
    }).catch((error) => {
      console.log("Error while updating user")
      console.log(error.response.data);
    })
  }


}
