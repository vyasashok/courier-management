export class UserDTO{
    
    private name : string;
    private token : string;
    private profile : string;
    private isTokenValid: string;

   
    get _name(){
        return this.name;
    }

    set _name(value){
        this.name = value;
    }


    get _token(){
        return this.token;
    }

    set _token(value){
        this.token = value;
    }

    get _profile(){
        return this.profile;
    }

    set _profile(value){
        this.profile = value;
    }

    get _isTokenValid(){
        return this.isTokenValid;
    }

    set _isTokenValid(value){
        this.isTokenValid = value;
    }
}