import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDoctorMonthlyPayment } from 'app/shared/model/doctor-monthly-payment.model';
import { DoctorMonthlyPaymentService } from './doctor-monthly-payment.service';

@Component({
  selector: 'jhi-doctor-monthly-payment-delete-dialog',
  templateUrl: './doctor-monthly-payment-delete-dialog.component.html'
})
export class DoctorMonthlyPaymentDeleteDialogComponent {
  doctorMonthlyPayment: IDoctorMonthlyPayment;

  constructor(
    protected doctorMonthlyPaymentService: DoctorMonthlyPaymentService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.doctorMonthlyPaymentService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'doctorMonthlyPaymentListModification',
        content: 'Deleted an doctorMonthlyPayment'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-doctor-monthly-payment-delete-popup',
  template: ''
})
export class DoctorMonthlyPaymentDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ doctorMonthlyPayment }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DoctorMonthlyPaymentDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.doctorMonthlyPayment = doctorMonthlyPayment;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/doctor-monthly-payment', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/doctor-monthly-payment', { outlets: { popup: null } }]);
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
