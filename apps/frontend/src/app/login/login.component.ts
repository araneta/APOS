import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
  });

  constructor(public authService: AuthService){

  }

  

  onSubmit(){
      console.log('form', this.loginForm.value);
      if(this.loginForm.invalid){
        return;
      }
      // Use non-null assertion (!) or default values to ensure types are `string`.
      const username = this.loginForm.value.username || '';
      const password = this.loginForm.value.password || '';
      
      this.authService.login(username, password);
    }
    
}
