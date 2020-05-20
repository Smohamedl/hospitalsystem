import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProvider } from 'app/shared/model/provider.model';
import { ProviderService } from './provider.service';

@Component({
  selector: 'jhi-Provider-delete-dialog',
  templateUrl: './provider-delete-dialog.component.html'
})
export class ProviderDeleteDialogComponent {
  provider: IProvider;

  constructor(protected providerService: ProviderService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.providerService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'ProviderListModification',
        content: 'Deleted an Provider'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-Provider-delete-popup',
  template: ''
})
export class ProviderDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ Provider }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ProviderDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.Provider = Provider;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/Provider', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/Provider', { outlets: { popup: null } }]);
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
