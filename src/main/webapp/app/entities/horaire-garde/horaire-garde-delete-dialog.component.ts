import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHoraire_garde } from 'app/shared/model/horaire-garde.model';
import { Horaire_gardeService } from './horaire-garde.service';

@Component({
  selector: 'jhi-horaire-garde-delete-dialog',
  templateUrl: './horaire-garde-delete-dialog.component.html'
})
export class Horaire_gardeDeleteDialogComponent {
  horaire_garde: IHoraire_garde;

  constructor(
    protected horaire_gardeService: Horaire_gardeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.horaire_gardeService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'horaire_gardeListModification',
        content: 'Deleted an horaire_garde'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-horaire-garde-delete-popup',
  template: ''
})
export class Horaire_gardeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ horaire_garde }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(Horaire_gardeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.horaire_garde = horaire_garde;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/horaire-garde', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/horaire-garde', { outlets: { popup: null } }]);
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
