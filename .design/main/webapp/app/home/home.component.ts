import {ElementSelectionService} from './../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../app/component-inspector.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account;
  authSubscription: Subscription;
  modalRef: NgbModalRef;

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,

    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private eventManager: JhiEventManager
  ) {this.__componentInspectorService.getComp(this);
}

  ngOnInit() {
    this.accountService.identity().subscribe((account: Account) => {
      this.account = account;
    });
    this.registerAuthenticationSuccess();
  }

  registerAuthenticationSuccess() {
    this.authSubscription = this.eventManager.subscribe('authenticationSuccess', () => {
      this.accountService.identity().subscribe(account => {
        this.account = account;
      });
    });
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  login() {
    this.modalRef = this.loginModalService.open();
  }

  ngOnDestroy() {
    if (this.authSubscription) {
      this.eventManager.destroy(this.authSubscription);
    }
  }
}
