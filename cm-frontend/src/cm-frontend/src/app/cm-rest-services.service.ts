import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Http, Response, RequestOptions, Headers } from "@angular/http";
import { Observable } from 'rxjs';
import {RegistrationDTO} from './dto/registrationDTO';
import {LoginDTO} from './dto/loginDTO';


@Injectable({
  providedIn: 'root'
})
export class CMRestServices {

  private serverUrl:string = "http://localhost:5001/api"; 
  
  private header = new Headers({
    'Content-Type' : 'application/json',
    'Cache-Control': 'no-cache'
  })

  constructor(private http:Http) { }



  registerNewUser(registartionObject:RegistrationDTO): Observable<Response>{
     return this.http.post(this.serverUrl + '/registration/save', registartionObject, {headers:this.header});
  }

  login(loginObject:LoginDTO): Observable<Response>{
    return this.http.post(this.serverUrl + '/login/authenticate', loginObject, {headers:this.header});
  }

  uploadFile(file:any):Observable<Response>{
    return this.http.post(this.serverUrl + '/file/uploadfile', file);
  }
}
