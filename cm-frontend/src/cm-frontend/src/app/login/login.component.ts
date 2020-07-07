import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormArray, Validators, FormBuilder } from '@angular/forms';
import {LoginService} from '../services/login.service';
import{LoginDTO} from '../dto/loginDTO';
import {UserDTO} from '../dto/userDTO';
import {LocalStorageService} from '../services/local-storage.service';
import { Routes, Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm:FormGroup;
  loginDTO:LoginDTO;
  userDTO:UserDTO;

  constructor(private formBuilder:FormBuilder, 
              private loginService:LoginService, 
              private localStorageService:LocalStorageService,
              private router:Router) { 

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

  get password(){
    return this.loginForm.get('password');
  }

  get email(){
    return this.loginForm.get('email');
  }

  onFormSubmit(){
    
    this.loginDTO = new LoginDTO();
    this.loginDTO._password = this.loginForm.value.password;
    this.loginDTO._email = this.loginForm.value.email;
    
    this.loginService.login(this.loginDTO).subscribe((response)=>{

      this.userDTO = new UserDTO();

      this.userDTO._name = response.json().name;
      this.userDTO._token = response.json().token;
      this.userDTO._isTokenValid = response.json().tokenValid;

      if(this.userDTO._isTokenValid){
            this.localStorageService.setToken(this.userDTO._token);
            this.router.navigate(["/dashboard"]);
      }
    })
  }

}
