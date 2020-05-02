import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAct } from 'app/shared/model/act.model';
import { ActService } from './act.service';

@Component({
  selector: 'jhi-act-delete-dialog',
  templateUrl: './act-delete-dialog.component.html'
})
export class ActDeleteDialogComponent {
  act: IAct;

  constructor(protected actService: ActService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.actService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'actListModification',
        content: 'Deleted an act'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-act-delete-popup',
  template: ''
})
export class ActDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ act }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ActDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.act = act;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/act', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/act', { outlets: { popup: null } }]);
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
