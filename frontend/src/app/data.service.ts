import {Injectable} from '@angular/core';
import {AxiosService} from "./axios.service";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class DataService {
  user: any;
  internalization: any;
  languages: any;
  categories: any;
  userCourses: any;

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

  public async getInternalizationFromServerWithLanguage(lang: any) {
    return this.axiosService.request(
      "GET",
      "/components/internationalization?language=" + lang || "English",
      {}
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

  public async getVideoUrlFromServer(url: string, file: any) {
    //TODO Fix this send in body or in param file
    const formData = new FormData();
    formData.append('video', file);
    formData.append('userId', this.user.id);

    return this.axiosService.requestWithHeaderAuthAndContentType(
      "POST",
      "/course/" + url + "/compress?username=" + this.user.username,
      {
        "video": file,
        "userId": this.user.id
      },
      this.user.secure_TOKEN,
      "multipart/form-data"
    )
  }

  async updateUser() {
    //TODO Change this to update user use user/update with data {username, password, }

    this.axiosService.requestWithHeaderAuth(
      "GET",
      "/user/update?username=" + this.user.username,
      null,
      this.user.secure_TOKEN
    ).then((response) => {
      if (response) {
        localStorage.removeItem("user");
        localStorage.setItem("user", JSON.stringify(response.data));
        this.user = JSON.parse(localStorage.getItem("user") || "{}");
      }
    }).catch((error) => {
      console.log("Error while updating user")
      console.log(error.response.data || error.response.data.message);
    })
  }

  async checkIsTokenValid() {
    if (!this.user.isEmpty) {
      return this.axiosService.requestWithHeaderAuth(
        "GET",
        "/is-secure-token-valid?userId=" + this.user.id,
        null,
        "Bearer " + this.user.secure_TOKEN
      ).then((response) => {

      }).catch((error) => {
        this.toLoginPage();
      })
    } else {
      console.log("User is not logged in");
      return false;
    }
  }

  async toLoginPage() {
    localStorage.clear();
    // send to login page if user is not logged in
    //TODO add redirect to login page with ?currentUrl= to redirect after login
    window.location.href = "/login";
  }

  async errorHandler(error: any) {
    switch (error.response.status) {
      case 401:
        if (error.response.data.message === "Invalid SECURE_TOKEN") {
          this.toLoginPage();
        }
        break;
      case 403:
        console.log("Forbidden");
        break;
    }
  }


  updateCourse(course: any) {
    this.axiosService.requestWithHeaderAuth(
      "POST",
      "/course/" + course.url + "/update?username=" + this.user.username,
      course,
      this.user.secure_TOKEN
    ).then((response) => {
      if (response) {
        console.log("Course updated");
      }
    }).catch((error) => {
      console.log(error.response.data.message);
    })
  }

  async joinCourse(courseUrl: any): Promise<any> {
    return this.axiosService.requestWithHeaderAuth(
      "POST",
      "/course/" + courseUrl + "/join?username=" + this.user.username,
      null,
      this.user.secure_TOKEN
    ).then((response) => {
      if (response) {
        return response;
      }
    }).catch((error) => {
      return error.response;
    })

  }

 async getCategoriesFromServer(lang: any) {
   return this.axiosService.request(
     "GET",
     "/components/categories?language=" + lang || "English",
     {}
   ).then((response) => {
     if (response) {
       localStorage.removeItem('categories');
       localStorage.setItem("categories", JSON.stringify(response.data));
       this.categories = JSON.parse(localStorage.getItem('categories') || '{}')
       return this.categories;
     }
   }).catch((error) => {
     console.log(error.response.data.message);
     throw error;
   })
  }

  getCategories(): Promise<any> {
    this.categories = JSON.parse(localStorage.getItem('categories') || '{}');
    return this.categories;
  }

  getListOfUserCoursesFromServer() {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
   return  this.axiosService.requestWithHeaderAuth(
      "GET",
      "user/courses?username=" + user.username,
      {},
      user.secure_TOKEN
    ).then((response: any) => {
      if (response) {
        localStorage.removeItem('userCourses');
        localStorage.setItem("userCourses", JSON.stringify(response.data));
        this.userCourses = JSON.parse(localStorage.getItem('userCourses') || '{}')
        return this.userCourses;
      }
    }).catch((error) => {
      if (error) {
        console.log(error.response.data);
      }
    });
  }

  getListsOfUserCourses() {
    this.userCourses = JSON.parse(localStorage.getItem('userCourses') || '{}');
    return this.userCourses;
  }
}
