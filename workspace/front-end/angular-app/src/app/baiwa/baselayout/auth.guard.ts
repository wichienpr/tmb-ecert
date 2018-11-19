import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../common/services';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    let url: string = state.url;
    return this.checkLogin(url);
  }

  canActivateChild(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {

    let url: string = state.url;
    return this.checkLogin(url);
  }

  checkLogin(url: string): | Promise<boolean> | boolean {

    // console.log("AuthGuard : ", url);

    if (this.authService.isLoggedIn) { return true; }

    // check login session
    return new Promise<boolean>((resolve, reject) => {
      this.authService.getUser().then(res => {
        if (res) {
          this.router.navigate(['/home']);
        } else {
          this.router.navigate(['/login']);
        }
        resolve(false);
      }).catch(() => {
        this.router.navigate(['/login']);
        resolve(false);
      });
    });
    // Navigate to the login page with extras
    // this.router.navigate(['/login']);
    // return false;
  }
}
