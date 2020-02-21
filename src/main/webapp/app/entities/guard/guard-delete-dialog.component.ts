import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGuard } from 'app/shared/model/guard.model';
import { GuardService } from './guard.service';

@Component({
  selector: 'jhi-guard-delete-dialog',
  templateUrl: './guard-delete-dialog.component.html'
})
export class GuardDeleteDialogComponent {
  guard: IGuard;

  constructor(protected guardService: GuardService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.guardService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'guardListModification',
        content: 'Deleted an guard'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-guard-delete-popup',
  template: ''
})
export class GuardDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ guard }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(GuardDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.guard = guard;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/guard', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/guard', { outlets: { popup: null } }]);
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
