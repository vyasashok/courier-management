export class LoginDTO{
    
    private email:string;
    private password:string;


    get _email(){
        return this.email;
    }

    set _email(value){
        this.email = value;
    }


    get _password(){
        return this.password;
    }

    set _password(value){
        this.password = value;
    }
    

}