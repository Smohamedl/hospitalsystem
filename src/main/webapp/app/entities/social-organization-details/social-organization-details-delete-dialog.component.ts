import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISocialOrganizationDetails } from 'app/shared/model/social-organization-details.model';
import { SocialOrganizationDetailsService } from './social-organization-details.service';

@Component({
  selector: 'jhi-social-organization-details-delete-dialog',
  templateUrl: './social-organization-details-delete-dialog.component.html'
})
export class SocialOrganizationDetailsDeleteDialogComponent {
  socialOrganizationDetails: ISocialOrganizationDetails;

  constructor(
    protected socialOrganizationDetailsService: SocialOrganizationDetailsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.socialOrganizationDetailsService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'socialOrganizationDetailsListModification',
        content: 'Deleted an socialOrganizationDetails'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-social-organization-details-delete-popup',
  template: ''
})
export class SocialOrganizationDetailsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ socialOrganizationDetails }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SocialOrganizationDetailsDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.socialOrganizationDetails = socialOrganizationDetails;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/social-organization-details', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/social-organization-details', { outlets: { popup: null } }]);
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
