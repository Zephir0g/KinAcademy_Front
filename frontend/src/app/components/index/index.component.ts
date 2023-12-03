import {Component, OnInit} from '@angular/core';
import {AxiosService} from "../../axios.service";
import {DataService} from "../../data.service";
import {NgxSpinnerService} from "ngx-spinner";

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

  isLoading: boolean = false;

  constructor(private axiosService: AxiosService, private data: DataService, private spinner: NgxSpinnerService) {
  }

  async ngOnInit() {
    this.spinner.show();
    this.isLoading = true;

    this.getData().then(() => {
      this.spinner.hide();
      this.isLoading = false;
    });

    try{
      await this.getData();
    } catch (error) {
      console.log(error);
    } finally {
      // Check if all data is available before hiding the spinner and updating isLoading
      if (this.user && this.internalization && this.languages) {
        this.spinner.hide();
        this.isLoading = false;
      }
    }
  }

  async getData() {
    try {
      await Promise.all([
        this.getUser(),
        this.getInternalization(),
        this.checkIsTokenValid(),
        this.getLanguage()
      ]);
    } catch (error) {
      console.error("Error occurred:", error);
    }
  }

  async getUser() {
    this.user = this.data.getUser()
    return this.user;
  }

  async getInternalization() {
    this.internalization = this.data.getInternalization()
    return this.internalization;
  }

  async getLanguage() {
    this.languages = this.data.getLanguages();
    return this.languages;
  }

  // Check if the user is logged in and if the token is valid
  async checkIsTokenValid() {
    if (this.user == null) {
      return this.axiosService.requestWithHeaderAuth(
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
    } else {
      return false;
    }
  }


}
