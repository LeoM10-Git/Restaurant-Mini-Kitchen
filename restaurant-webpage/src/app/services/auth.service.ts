import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Router } from "@angular/router";
import { User } from "../models/user.model";
import { catchError, EMPTY, lastValueFrom, tap, throwError } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  API_URL = '/api/user'
  constructor(private http:HttpClient,
              private router: Router) { }


  signup(user: User): Promise<any> {
    return lastValueFrom(this.http.post(`${this.API_URL}/register`, user))
  }

  // login accept x-www-form-urlencoded
  login(email: string, password: string): Promise<any>{
    const user = new HttpParams()
      .set ('email', email)
      .set ('password', password)

    const headers = new HttpHeaders()
      .set('Content-Type', 'application/x-www-form-urlencoded')
    return lastValueFrom(this.http.post(`${this.API_URL}/login`, user.toString(), {headers}))
  }

  logout() {
    this.removeSession();
    this.router.navigate(['/login']).then();
  }

  /*Verify the user access*/
  userAuth() :Promise<any>{
    const token = this.getAccessToken();
    return lastValueFrom(this.http.get(`${this.API_URL}/auth/verify`,  {
        headers: {
          Authorization: `Bearer ${token}`,
        }, responseType: 'text'}))
    }

  getRememberMeToken() {
    lastValueFrom(this.http.get(`${this.API_URL}/remember-me`)).then((response: any) => {
        this.setAccessToken(response['access_token'])
        return true;
      }).catch((error: any) => {
      throwError(() => new Error(error))
      return false;
    })
  }

  getNewAccessToken() {
     const token = this.getRefreshToken();
     const headers = new HttpHeaders({Authorization: `Bearer ${ token }`})
    return this.http.get(`${this.API_URL}/get-refresh-token`, {headers: headers})
      .pipe(
    tap((res: any) => {
      this.setAccessToken(res['access_token'])
    }),
        catchError((err) => {
          console.log(err)
          return EMPTY;
        }))
  }

  /*Deal with the local storage for the tokens*/
  getAccessToken(){
    return localStorage.getItem('mini-kitchen-access');
  }

  setAccessToken(accessToken: string) {
    return localStorage.setItem('mini-kitchen-access',accessToken)
  }

  getRefreshToken(){
    return localStorage.getItem('mini-kitchen-refresh-access');
  }

  setRefreshToken(refreshToken: string) {
    return localStorage.setItem('mini-kitchen-refresh-access',refreshToken)
  }

  removeSession() {
    localStorage.removeItem('mini-kitchen-refresh-access')
    localStorage.removeItem('mini-kitchen-access')
  }
  removeAccessToken() {
    localStorage.removeItem('mini-kitchen-access')
  }

}
