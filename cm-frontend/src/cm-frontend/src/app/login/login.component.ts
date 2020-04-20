import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormArray, Validators, FormBuilder } from '@angular/forms';
import {LoginService} from '../services/login.service';
import{LoginDTO} from '../dto/loginDTO';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm:FormGroup;
  loginDTO:LoginDTO;

  constructor(private formBuilder:FormBuilder, private loginService:LoginService) { 

  }

  ngOnInit() {
    this.createLoginForm();
  }

  createLoginForm(){
    this.loginForm = this.formBuilder.group({
        email:["", [Validators.email, Validators.required]],
        password:["", Validators.required]
    })
  }

  get name(){
    return this.loginForm.get('name');
  }

  get email(){
    return this.loginForm.get('email');
  }

  onFormSubmit(){
    
    this.loginDTO = new LoginDTO();
    this.loginDTO._password = this.loginForm.value.password;
    this.loginDTO._email = this.loginForm.value.email;
    
    this.loginService.login(this.loginDTO).subscribe((response)=>{
      console.log(response.json())
    })
  }

}
