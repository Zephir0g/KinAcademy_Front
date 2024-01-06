import {Injectable} from '@angular/core';
import axios from "axios";

@Injectable({
  providedIn: 'root'
})
export class AxiosService {

  constructor() {
    axios.defaults.baseURL = 'http://26.8.137.161:8080/api/v1';
    // axios.defaults.baseURL = 'http://192.168.0.104:8080/api/v1';
    axios.defaults.headers.post['Content-Type'] = 'application/json';
  }

  request(method: string, url: string, data: any): Promise<any> {
    return axios({
      method: method,
      url: url,
      data: data
    });
  }

  requestWithHeaderAuth(method: string, url: string, data: any, TOKEN: string): Promise<any> {

    return axios({
      method: method,
      url: url,
      data: data,
      headers: {
        'Authorization': 'Bearer ' + TOKEN
      }
    })
  }

  requestWithHeaderAuthAndContentType(method: string, url: string, data: any, TOKEN: string, contentType: string): Promise<any> {

    return axios({
      method: method,
      url: url,
      data: data,
      headers: {
        'Authorization': 'Bearer ' + TOKEN,
        'Content-Type': contentType
      }
    })

  }

  async getVideo(token: string, url: string): Promise<any> {
    return axios.get(
      url,
      {
        responseType: 'blob',
        headers: {
          'Authorization': 'Bearer ' + token
        }
      });
  }
}
