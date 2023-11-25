import {Component} from '@angular/core';
import {AxiosService} from "../../axios.service";

@Component({
  selector: 'app-authentication',
  templateUrl: './authentication.component.html',
  styleUrls: ['./authentication.component.css']
})
export class AuthenticationComponent {
  data: string[] = [];

  constructor(private axiosService: AxiosService) {
  }

  ngOnInit(): void {
    this.axiosService.request(
      "GET",
      "/api/test",
      {}
    ).then(
      (response: any) => {
        this.data = response.data;
      }
    )
  }
}
