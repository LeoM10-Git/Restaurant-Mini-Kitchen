import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { lastValueFrom } from "rxjs";
import { User } from "../models/user.model";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  API_URL = '/api/user/resources/user-account'
  constructor(private http: HttpClient) { }

  getAccountInfo() :Promise<User>{
    return lastValueFrom(this.http.get(this.API_URL)) as Promise<User>;
  }
}
