import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HospitalsystemTestModule } from '../../../test.module';
import { QuantityPriceDeleteDialogComponent } from 'app/entities/quantity-price/quantity-price-delete-dialog.component';
import { QuantityPriceService } from 'app/entities/quantity-price/quantity-price.service';

describe('Component Tests', () => {
  describe('QuantityPrice Management Delete Component', () => {
    let comp: QuantityPriceDeleteDialogComponent;
    let fixture: ComponentFixture<QuantityPriceDeleteDialogComponent>;
    let service: QuantityPriceService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [QuantityPriceDeleteDialogComponent]
      })
        .overrideTemplate(QuantityPriceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(QuantityPriceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(QuantityPriceService);
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
