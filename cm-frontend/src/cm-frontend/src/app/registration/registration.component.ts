import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, FormArray, Validators, FormBuilder } from '@angular/forms';
import {RegistrationDTO} from '../dto/registrationDTO';
import {RegistrationService} from '../services/registration.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit, OnDestroy {

  registrationForm:FormGroup;
  registrationDTO:RegistrationDTO;
  registrationErrorMessage:string;
  somethingWentWrongMessage:string;
  registrationSuccessMessage:string;
  registartionFormSubscription:any;

  constructor(private formBuilder:FormBuilder,
              private registrationService: RegistrationService) 
              { 

  }

  ngOnInit() {
    this.createRegistrationForm();

    this.registartionFormSubscription = this.registrationForm.valueChanges.subscribe(res=>{
      this.registrationErrorMessage=null;
      this.somethingWentWrongMessage=null;
      this.registrationSuccessMessage=null;
    })
  }

  createRegistrationForm(){
    this.registrationForm = this.formBuilder.group({
        name:["", Validators.required],
        email:["", [Validators.email, Validators.required]],
        phone:["", Validators.required],
        password:["", Validators.required]
    })
  }

  get name(){
    return this.registrationForm.get('name');
  }

  get email(){
    return this.registrationForm.get('email');
  }

  get phone(){
    return this.registrationForm.get('phone');
  }

  get password(){
    return this.registrationForm.get('password');
  }

  onFormSubmit(){

    this.registrationErrorMessage=null;
    this.somethingWentWrongMessage=null;
    this.registrationSuccessMessage=null;

    this.registrationDTO = new RegistrationDTO();
    this.registrationDTO._name = this.registrationForm.value.name;
    this.registrationDTO._email = this.registrationForm.value.email;
    this.registrationDTO._phone = this.registrationForm.value.phone;
    this.registrationDTO._password = this.registrationForm.value.password;

    this.registrationService.registerNewUser(this.registrationDTO).subscribe(response=>{

      if(response.json()){
            this.registrationSuccessMessage ='Registration success!!!'
      }
      else{
            this.registrationErrorMessage = 'User already exist!!!'
      }


    }, error=>{
         this.somethingWentWrongMessage = 'Something went wrong!!!'
    })
  }

  uploadFile(file){

    let formData = new FormData();
    formData.append("file", file[0]);
    this.registrationService.uploadFile(formData).subscribe(response=>{

    })
  }

  ngOnDestroy(){
    this.registartionFormSubscription.unsubscribe();
  }

}
