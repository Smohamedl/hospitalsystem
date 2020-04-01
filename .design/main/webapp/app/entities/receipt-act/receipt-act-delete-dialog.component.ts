import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReceiptAct } from 'app/shared/model/receipt-act.model';
import { ReceiptActService } from './receipt-act.service';

@Component({
  selector: 'jhi-receipt-act-delete-dialog',
  templateUrl: './receipt-act-delete-dialog.component.html'
})
export class ReceiptActDeleteDialogComponent {
  receiptAct: IReceiptAct;

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,

    protected receiptActService: ReceiptActService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {this.__componentInspectorService.getComp(this);
}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.receiptActService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'receiptActListModification',
        content: 'Deleted an receiptAct'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-receipt-act-delete-popup',
  template: ''
})
export class ReceiptActDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ receiptAct }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ReceiptActDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.receiptAct = receiptAct;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/receipt-act', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/receipt-act', { outlets: { popup: null } }]);
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
