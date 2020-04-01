import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HospitalsystemTestModule } from '../../../test.module';
import { ReceiptActDeleteDialogComponent } from 'app/entities/receipt-act/receipt-act-delete-dialog.component';
import { ReceiptActService } from 'app/entities/receipt-act/receipt-act.service';

describe('Component Tests', () => {
  describe('ReceiptAct Management Delete Component', () => {
    let comp: ReceiptActDeleteDialogComponent;
    let fixture: ComponentFixture<ReceiptActDeleteDialogComponent>;
    let service: ReceiptActService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [ReceiptActDeleteDialogComponent]
      })
        .overrideTemplate(ReceiptActDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReceiptActDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReceiptActService);
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
