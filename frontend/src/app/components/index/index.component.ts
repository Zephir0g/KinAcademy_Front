import {Component, OnInit} from '@angular/core';
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
  categories: any;
  internalization: any;

  isPageLoading: boolean = false;

  constructor(private data: DataService, private spinner: NgxSpinnerService) {
  }

  async ngOnInit() {
    this.isPageLoading = true;
    this.spinner.show().then(r => {
      this.getData().then(() => {
        this.spinner.hide();
        this.isPageLoading = false;
      });
    });
  }

  async getData() {
    try {
      await Promise.all([
        this.getUser(),
        this.getInternalization(),
        //this.data.checkIsTokenValid(),
        this.getLanguage(),
        this.getCategories()
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

  async getCategories() {
    this.categories = this.data.getCategoriesFromServer(this.user.language || "English");
    return this.categories;
  }


  // Check if the user is logged in and if the token is valid


  protected readonly JSON = JSON;
}
