import { Injectable } from '@angular/core';
import { flatMap } from 'rxjs/operators';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class LoginService {
  constructor(private accountService: AccountService, private authServerProvider: AuthServerProvider, private http: HttpClient) {}

  login(credentials) {
    return this.authServerProvider.login(credentials).pipe(flatMap(() => this.accountService.identity(true)));
  }

  logout() {
    function logoutfunc(res) {
      console.log('Clear Session');
    }

    if (JSON.parse(localStorage.getItem('User_Session')) != null) {
      this.http.get('api/logout', { observe: 'response' }).subscribe(res => {
        logoutfunc(res);
      });
      this.authServerProvider.logout().subscribe(null, null, () => this.accountService.authenticate(null));
    }
  }
}
