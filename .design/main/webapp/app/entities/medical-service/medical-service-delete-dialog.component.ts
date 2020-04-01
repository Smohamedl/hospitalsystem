import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMedicalService } from 'app/shared/model/medical-service.model';
import { MedicalServiceService } from './medical-service.service';

@Component({
  selector: 'jhi-medical-service-delete-dialog',
  templateUrl: './medical-service-delete-dialog.component.html'
})
export class MedicalServiceDeleteDialogComponent {
  medicalService: IMedicalService;

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,

    protected medicalServiceService: MedicalServiceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {this.__componentInspectorService.getComp(this);
}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.medicalServiceService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'medicalServiceListModification',
        content: 'Deleted an medicalService'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-medical-service-delete-popup',
  template: ''
})
export class MedicalServiceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ medicalService }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MedicalServiceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.medicalService = medicalService;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/medical-service', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/medical-service', { outlets: { popup: null } }]);
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
