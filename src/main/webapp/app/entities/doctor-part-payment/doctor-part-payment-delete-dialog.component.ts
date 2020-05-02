import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDoctorPartPayment } from 'app/shared/model/doctor-part-payment.model';
import { DoctorPartPaymentService } from './doctor-part-payment.service';

@Component({
  selector: 'jhi-doctor-part-payment-delete-dialog',
  templateUrl: './doctor-part-payment-delete-dialog.component.html'
})
export class DoctorPartPaymentDeleteDialogComponent {
  doctorPartPayment: IDoctorPartPayment;

  constructor(
    protected doctorPartPaymentService: DoctorPartPaymentService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.doctorPartPaymentService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'doctorPartPaymentListModification',
        content: 'Deleted an doctorPartPayment'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-doctor-part-payment-delete-popup',
  template: ''
})
export class DoctorPartPaymentDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ doctorPartPayment }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DoctorPartPaymentDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.doctorPartPayment = doctorPartPayment;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/doctor-part-payment', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/doctor-part-payment', { outlets: { popup: null } }]);
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
