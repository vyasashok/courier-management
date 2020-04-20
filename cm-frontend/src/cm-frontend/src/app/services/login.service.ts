import { Injectable } from '@angular/core';
import {CMRestServices} from '../../app/cm-rest-services.service';
import { Observable } from 'rxjs';
import {LoginDTO} from '../dto/loginDTO';
import { Http, Response, RequestOptions, Headers } from "@angular/http";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private cmRestService:CMRestServices) { }

  login(loginObject:LoginDTO) : Observable<Response>{
       return this.cmRestService.login(loginObject);
  }


}
