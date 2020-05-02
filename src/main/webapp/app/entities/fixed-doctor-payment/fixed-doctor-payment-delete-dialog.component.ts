import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFixedDoctorPayment } from 'app/shared/model/fixed-doctor-payment.model';
import { FixedDoctorPaymentService } from './fixed-doctor-payment.service';

@Component({
  selector: 'jhi-fixed-doctor-payment-delete-dialog',
  templateUrl: './fixed-doctor-payment-delete-dialog.component.html'
})
export class FixedDoctorPaymentDeleteDialogComponent {
  fixedDoctorPayment: IFixedDoctorPayment;

  constructor(
    protected fixedDoctorPaymentService: FixedDoctorPaymentService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.fixedDoctorPaymentService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'fixedDoctorPaymentListModification',
        content: 'Deleted an fixedDoctorPayment'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-fixed-doctor-payment-delete-popup',
  template: ''
})
export class FixedDoctorPaymentDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ fixedDoctorPayment }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(FixedDoctorPaymentDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.fixedDoctorPayment = fixedDoctorPayment;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/fixed-doctor-payment', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/fixed-doctor-payment', { outlets: { popup: null } }]);
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
