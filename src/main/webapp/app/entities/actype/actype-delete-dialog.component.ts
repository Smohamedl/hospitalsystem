import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IActype } from 'app/shared/model/actype.model';
import { ActypeService } from './actype.service';

@Component({
  selector: 'jhi-actype-delete-dialog',
  templateUrl: './actype-delete-dialog.component.html'
})
export class ActypeDeleteDialogComponent {
  actype: IActype;

  constructor(protected actypeService: ActypeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.actypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'actypeListModification',
        content: 'Deleted an actype'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-actype-delete-popup',
  template: ''
})
export class ActypeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ actype }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ActypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.actype = actype;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/actype', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/actype', { outlets: { popup: null } }]);
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
