import {Injectable} from '@angular/core';
import axios from "axios";

@Injectable({
  providedIn: 'root'
})
export class AxiosService {

  constructor() {
    axios.defaults.baseURL = 'http://26.8.137.161:8080/api';
    // axios.defaults.baseURL = 'http://192.168.0.104:8080/api';
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
