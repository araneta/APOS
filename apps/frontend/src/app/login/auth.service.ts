import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({providedIn:'root'})
export class AuthService{
    constructor(private http: HttpClient){

    }

    login (email: string, password: string){
        const authData = {username: email, password: password};
        this.http.post("http://localhost:8888/api/auth/login", authData)
        .subscribe(response=>{
            console.log(response);
        });
    }
}