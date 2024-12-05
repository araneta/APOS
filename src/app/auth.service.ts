import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }

  addData(newData: string):string {
    return "uu"+newData;
  }
}
