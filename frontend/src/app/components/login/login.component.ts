import {Component, OnInit} from '@angular/core';
import {AxiosService} from "../../axios.service";
import {ActivatedRoute, Router} from '@angular/router';
import {DataService} from "../../data.service";
import {NgxSpinnerService} from "ngx-spinner";
import {MessageService, PrimeNGConfig} from "primeng/api";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [MessageService]
})
export class LoginComponent implements OnInit {

  constructor(private axiosService: AxiosService,
              private router: Router,
              private messageService: MessageService,
              private primengConfig: PrimeNGConfig,
              private data: DataService,
              private route: ActivatedRoute,
              private spinner: NgxSpinnerService) {
  }

  ngOnInit(): void {
    // Get the user's preferred languages
    this.primengConfig.ripple = true;
    const userLangs = navigator.languages;

    // Create a display name object for languages
    const languageNames = new Intl.DisplayNames(['en'], {type: 'language'});

    // Extract and clean the language names and assign the first language name
    this.userLanguage = languageNames.of(userLangs[0]?.split('-')[0]) || 'English';

  }

  active: string = "login";
  username: string = "";
  password: string = "";
  email: string = "";
  firstName: String = "";
  surname: String = "";
  userLanguage: string = "";

  internalization: any = {};
  isError: boolean = false;
  params: string = "";


  tabSwitch(tab: string): void {
    this.active = tab;
    this.password = "";
    this.username = "";
  }


  onSubmitLogin(): void {
    this.spinner.show();
    if (this.username == "" || this.password == "") {
      this.isError = true;
      this.messageService.add({severity: 'error', summary: 'Error', detail: 'Please fill required fields'});
      this.spinner.hide();
      return;
    }

    this.checkParams();


    this.axiosService.request(
      "POST",
      "/auth/login",
      {
        "username": this.username,
        "password": this.password
      },
    ).catch((error) => {
      this.spinner.hide();
      this.isError = true;
      this.messageService.add({severity: 'error', summary: 'Error', detail: error.response.data});
    })
      .then((response) => {
        if (response) {
          localStorage.setItem('user', JSON.stringify(response.data));


          this.data.getInternalizationFromServerWithLanguage(response.data.language).then((internalization) => {
            this.data.getLanguagesFromServer().then((languages) => {
              if(this.params !== "") {
                this.router.navigate([this.params]);
              }else {
                this.router.navigate(['/']);
              }
            });
          });
        }
      })
  }

  onSubmitRegister() {
    this.spinner.show();

    if (this.username == "" || this.password == "" || this.email == "" || this.firstName == "" || this.surname == "") {
      this.isError = true;
      this.messageService.add({severity: 'error', summary: 'Error', detail: 'Please fill required fields'});
      this.spinner.hide();
      return;
    }

    this.checkParams();
    this.axiosService.request(
      "POST",
      "/auth/register",
      {
        "firstName": this.firstName,
        "surname": this.surname,
        "username": this.username,
        "email": this.email,
        "password": this.password,
        "language": this.userLanguage || "English"
      },
    ).catch((error) => {

      this.spinner.hide();
      this.isError = true;
      this.messageService.add({severity: 'error', summary: 'Error', detail: error.response.data});
    }).then((response) => {
      if (response) {
        this.spinner.hide().then(r => this.active = "login");
      }
    })
  }

  checkParams() {
    this.route.queryParams.subscribe(params => {
      // Use switch to handle different query parameters
      switch (true) {
        case 'course' in params:
          const courseParam = params['course'];
          this.params = "/course/" + courseParam;
          break;
      }
    });
  }


}

