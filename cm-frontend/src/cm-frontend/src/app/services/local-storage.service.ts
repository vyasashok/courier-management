import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  static tokenKey:string = "token";  

  constructor() { 

  }

  getToken(){
      return JSON.parse(localStorage.getItem(LocalStorageService.tokenKey));
  }

  setToken(token:string){
      localStorage.setItem(LocalStorageService.tokenKey, JSON.stringify(token));
  }


}