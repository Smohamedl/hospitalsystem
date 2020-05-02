import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IQuantityPrice } from 'app/shared/model/quantity-price.model';
import { QuantityPriceService } from './quantity-price.service';

@Component({
  selector: 'jhi-quantity-price-delete-dialog',
  templateUrl: './quantity-price-delete-dialog.component.html'
})
export class QuantityPriceDeleteDialogComponent {
  quantityPrice: IQuantityPrice;

  constructor(
    protected quantityPriceService: QuantityPriceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.quantityPriceService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'quantityPriceListModification',
        content: 'Deleted an quantityPrice'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-quantity-price-delete-popup',
  template: ''
})
export class QuantityPriceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ quantityPrice }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(QuantityPriceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.quantityPrice = quantityPrice;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/quantity-price', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/quantity-price', { outlets: { popup: null } }]);
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
