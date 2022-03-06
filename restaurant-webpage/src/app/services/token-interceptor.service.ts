import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from "@angular/common/http";
import {
  catchError,
  EMPTY,
  Observable, Subject,
  switchMap,
  tap,
} from 'rxjs';
import { AuthService } from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class TokenInterceptorService implements HttpInterceptor {

  refreshingAccessToken = false
  accessTokenRefreshed: Subject<any> = new Subject();


  constructor(private authService: AuthService,) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Handler the request
    request = this.addAuthHeader(request);
    return next.handle(request)
      .pipe(
        catchError((error: HttpErrorResponse) => {
            if ( (error.status === 403 || error.status === 401) && !this.refreshingAccessToken) {
              // refresh the access token
              /*remove the access token first, to prevent the intercept
              use the expired access token on the header to get refresh token, which will fail
              !important*/
              this.authService.removeAccessToken()

              return this.refreshAccessToken()
                .pipe(
                  switchMap(() => {
                    request = this.addAuthHeader(request);
                    return next.handle(request);
                  }),
                  catchError((err: any) => {
                    // this.authService.logout();
                    return EMPTY;
                  })
                )
            }
            return EMPTY;
          }))
  }

 refreshAccessToken() {
    if ( this.refreshingAccessToken ) {
      this.refreshingAccessToken = false;
      return new Observable(observer => {
        this.accessTokenRefreshed.subscribe(() => {
          // this code will run when the access token has been refreshed
          observer.next();
          observer.complete();
        })
      })
    } else {
      // call a method in the auth service to send a request to refresh the access token
      this.refreshingAccessToken = true;
      return this.authService.getNewAccessToken()
        .pipe(
          tap(() => {
            this.refreshingAccessToken = false;
            this.accessTokenRefreshed.next(null);
            console.log('Access token refreshed');
          })
        )
    }
  }


  addAuthHeader(req: HttpRequest<any>) {
    //get access token
    const accessToken = this.authService.getAccessToken();
    // append the access token to the request header
    if ( accessToken ) {
      return req.clone({setHeaders: {Authorization: `Bearer ${ accessToken }`}});
    }
    return req;
  }
}
