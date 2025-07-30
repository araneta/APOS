import { Component } from '@angular/core';
import {FormGroup, FormControl} from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ReactiveFormsModule],

  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  
  profileForm = new FormGroup({
    firstName: new FormControl('fname'),
    lastName: new FormControl('lname'),
  });

  teloForm = new FormGroup({
    firstName: new FormControl('mantab'),
    lastName: new FormControl('tres'),
  });

  constructor(){

  }
  onSubmit(){
    console.log(this.profileForm.value);
  }

  onSubmitTelo(){
    console.log('das',this.teloForm.value);
  }
}
