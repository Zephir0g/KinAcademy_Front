import {Component, OnInit} from '@angular/core';
import {AxiosService} from "../../axios.service";
import {Router} from '@angular/router';
import {DataService} from "../../data.service";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private axiosService: AxiosService,
              private router: Router,
              private data: DataService,
              private spinner: NgxSpinnerService) {
  }

  ngOnInit(): void {
    // Get the user's preferred languages
    const userLangs = navigator.languages;

    // Create a display name object for languages
    const languageNames = new Intl.DisplayNames(['en'], {type: 'language'});

    // Extract and clean the language names and assign the first language name
    this.userLanguage = languageNames.of(userLangs[0]?.split('-')[0]) || 'English';

  }

  active: string = "login";
  login: string = "";
  password: string = "";
  email: string = "";
  firstName: String = "";
  surname: String = "";
  userLanguage: string = "";

  internalization: any = {};

  errorMessages: string[] = [];


  tabSwitch(tab: string): void {
    this.active = tab;
  }

  onSubmitLogin(): void {
    this.spinner.show();
    this.errorMessages = [];
    if (this.login == "" || this.password == "") {
      this.errorMessages.push("Please fill required fields")
      return;
    }
    this.axiosService.request(
      "POST",
      "/login",
      {
        "login": this.login,
        "password": this.password
      },
    ).catch((error) => {
      // this.errorMessages.push(error.response.data.message)
      console.log(error.response)
      //TODO fix if error show error message not load data
    })
      .then((response) => {
        if (response) {
          localStorage.setItem('user', JSON.stringify(response.data));

          this.data.getInternalizationFromServerWithLanguage(response.data.language);
          this.data.getLanguagesFromServer();

          window.location.href = "/";
        }
      })
  }

  onSubmitRegister() {
    this.errorMessages = [];
    this.axiosService.request(
      "POST",
      "/register",
      {
        "firstName": this.firstName,
        "surname": this.surname,
        "login": this.login,
        "email": this.email,
        "password": this.password,
        "language": this.userLanguage || "English"
      },
    ).catch((error) => {
      this.errorMessages.push(error.response.data.message)
    }).then((response) => {
      if (response) {
        localStorage.setItem("user", JSON.stringify(response.data));
        // redirect to home page if login is successful
        window.location.href = "/";
      }
    })
  }


}

