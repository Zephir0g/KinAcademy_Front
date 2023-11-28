import {Injectable, Input} from '@angular/core';
import axios from "axios";

@Injectable({
  providedIn: 'root'
})
export class AxiosService {

  constructor() {
    axios.defaults.baseURL = 'http://localhost:8080/api';
    axios.defaults.headers.post['Content-Type'] = 'application/json';
  }

  request(method: string, url: string, data: any): Promise<any> {
    return axios({
      method: method,
      url: url,
      data: data
    });
  }

  requestWithHeaderAuth(method: string, url: string, data: any, header: string): Promise<any> {

    return axios({
      method: method,
      url: url,
      data: data,
      headers: {
        'Authorization': 'Bearer ' + header
      }
    })
  }

  requestWithHeaderLang(method: string, url: string, data: any, header: string): Promise<any> {
    return axios({
      method: method,
      url: url,
      data: data,
      headers: {
        'Accept-Language': header
      }
    })
  }

}
