import { Injectable } from '@angular/core';
import {CMRestServices} from '../../app/cm-rest-services.service';
import { Observable } from 'rxjs';
import {RegistrationDTO} from '../dto/registrationDTO';
import { Http, Response, RequestOptions, Headers } from "@angular/http";

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private cmRestService:CMRestServices) { }

  registerNewUser(registrationObject:RegistrationDTO) : Observable<Response>{
       return this.cmRestService.registerNewUser(registrationObject);
  }


}
