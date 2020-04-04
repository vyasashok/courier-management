import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormArray, Validators, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm:FormGroup;

  constructor(private formBuilder:FormBuilder) { 

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

  onFormSubmit(){
    console.log(this.loginForm);
  }

}
