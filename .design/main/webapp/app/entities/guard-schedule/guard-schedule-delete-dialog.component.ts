import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGuardSchedule } from 'app/shared/model/guard-schedule.model';
import { GuardScheduleService } from './guard-schedule.service';

@Component({
  selector: 'jhi-guard-schedule-delete-dialog',
  templateUrl: './guard-schedule-delete-dialog.component.html'
})
export class GuardScheduleDeleteDialogComponent {
  guardSchedule: IGuardSchedule;

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,

    protected guardScheduleService: GuardScheduleService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {this.__componentInspectorService.getComp(this);
}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.guardScheduleService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'guardScheduleListModification',
        content: 'Deleted an guardSchedule'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-guard-schedule-delete-popup',
  template: ''
})
export class GuardScheduleDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ guardSchedule }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(GuardScheduleDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.guardSchedule = guardSchedule;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/guard-schedule', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/guard-schedule', { outlets: { popup: null } }]);
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
