import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISocialOrganization } from 'app/shared/model/social-organization.model';
import { SocialOrganizationService } from './social-organization.service';

@Component({
  selector: 'jhi-social-organization-delete-dialog',
  templateUrl: './social-organization-delete-dialog.component.html'
})
export class SocialOrganizationDeleteDialogComponent {
  socialOrganization: ISocialOrganization;

  constructor(
    protected socialOrganizationService: SocialOrganizationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.socialOrganizationService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'socialOrganizationListModification',
        content: 'Deleted an socialOrganization'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-social-organization-delete-popup',
  template: ''
})
export class SocialOrganizationDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ socialOrganization }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SocialOrganizationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.socialOrganization = socialOrganization;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/social-organization', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/social-organization', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
