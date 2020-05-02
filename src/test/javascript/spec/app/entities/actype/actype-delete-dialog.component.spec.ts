import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HospitalsystemTestModule } from '../../../test.module';
import { ActypeDeleteDialogComponent } from 'app/entities/actype/actype-delete-dialog.component';
import { ActypeService } from 'app/entities/actype/actype.service';

describe('Component Tests', () => {
  describe('Actype Management Delete Component', () => {
    let comp: ActypeDeleteDialogComponent;
    let fixture: ComponentFixture<ActypeDeleteDialogComponent>;
    let service: ActypeService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [ActypeDeleteDialogComponent]
      })
        .overrideTemplate(ActypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ActypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ActypeService);
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
