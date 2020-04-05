import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProvidedr } from 'app/shared/model/provider.model';
import { ProviderService } from './provider.service';

@Component({
  selector: 'jhi-providedr-delete-dialog',
  templateUrl: './provider-delete-dialog.component.html'
})
export class ProviderDeleteDialogComponent {
  providedr: IProvidedr;

  constructor(protected providedrService: ProviderService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.providedrService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'providedrListModification',
        content: 'Deleted an providedr'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-providedr-delete-popup',
  template: ''
})
export class ProvidedrDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ providedr }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ProviderDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.providedr = providedr;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/providedr', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/providedr', { outlets: { popup: null } }]);
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
