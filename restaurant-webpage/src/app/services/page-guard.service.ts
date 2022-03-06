import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { AuthService } from "./auth.service";

@Injectable(
  {
    providedIn: 'root'
}
)
export class PageGuardService implements CanActivate {

  constructor(private router: Router,
              private authService: AuthService) {
  }

  canActivate(activatedRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    let canActivate = true;
    this.authService.userAuth().then((response) => {
      if ( response === "Verified" ) {
        canActivate = true;
      }
      else {
        canActivate = false;
        this.authService.logout()
        this.router.navigate(['login']).then();
      }
    }).catch((error) => {
      canActivate = false
      this.authService.logout()
      this.router.navigate(['login']).then();
    })
    return canActivate;
  }
}
