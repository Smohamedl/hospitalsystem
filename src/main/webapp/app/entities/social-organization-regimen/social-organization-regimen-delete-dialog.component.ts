import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISocialOrganizationRegimen } from 'app/shared/model/social-organization-regimen.model';
import { SocialOrganizationRegimenService } from './social-organization-regimen.service';

@Component({
  selector: 'jhi-social-organization-regimen-delete-dialog',
  templateUrl: './social-organization-regimen-delete-dialog.component.html'
})
export class SocialOrganizationRegimenDeleteDialogComponent {
  socialOrganizationRegimen: ISocialOrganizationRegimen;

  constructor(
    protected socialOrganizationRegimenService: SocialOrganizationRegimenService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.socialOrganizationRegimenService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'socialOrganizationRegimenListModification',
        content: 'Deleted an socialOrganizationRegimen'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-social-organization-regimen-delete-popup',
  template: ''
})
export class SocialOrganizationRegimenDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ socialOrganizationRegimen }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SocialOrganizationRegimenDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.socialOrganizationRegimen = socialOrganizationRegimen;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/social-organization-regimen', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/social-organization-regimen', { outlets: { popup: null } }]);
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
