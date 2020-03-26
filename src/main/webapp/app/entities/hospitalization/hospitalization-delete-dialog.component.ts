import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHospitalization } from 'app/shared/model/hospitalization.model';
import { HospitalizationService } from './hospitalization.service';

@Component({
  selector: 'jhi-hospitalization-delete-dialog',
  templateUrl: './hospitalization-delete-dialog.component.html'
})
export class HospitalizationDeleteDialogComponent {
  hospitalization: IHospitalization;

  constructor(
    protected hospitalizationService: HospitalizationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.hospitalizationService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'hospitalizationListModification',
        content: 'Deleted an hospitalization'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-hospitalization-delete-popup',
  template: ''
})
export class HospitalizationDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ hospitalization }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(HospitalizationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.hospitalization = hospitalization;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/hospitalization', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/hospitalization', { outlets: { popup: null } }]);
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
