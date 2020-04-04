export class RegistrationDTO{
    
    private name:string;
    private email:string;
    private phone:string;
    private password:string;

    get _name(){
        return this.name;
    }

    set _name(value){
        this.name = value;
    }

    get _email(){
        return this.email;
    }

    set _email(value){
        this.email = value;
    }

    get _phone(){
        return this.phone;
    }

    set _phone(value){
        this.phone = value;
    }

    get _password(){
        return this.password;
    }

    set _password(value){
        this.password = value;
    }
    

}