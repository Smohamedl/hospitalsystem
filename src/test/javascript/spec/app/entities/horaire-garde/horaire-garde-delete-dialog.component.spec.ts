import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HospitalsystemTestModule } from '../../../test.module';
import { Horaire_gardeDeleteDialogComponent } from 'app/entities/horaire-garde/horaire-garde-delete-dialog.component';
import { Horaire_gardeService } from 'app/entities/horaire-garde/horaire-garde.service';

describe('Component Tests', () => {
  describe('Horaire_garde Management Delete Component', () => {
    let comp: Horaire_gardeDeleteDialogComponent;
    let fixture: ComponentFixture<Horaire_gardeDeleteDialogComponent>;
    let service: Horaire_gardeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [Horaire_gardeDeleteDialogComponent]
      })
        .overrideTemplate(Horaire_gardeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(Horaire_gardeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(Horaire_gardeService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
