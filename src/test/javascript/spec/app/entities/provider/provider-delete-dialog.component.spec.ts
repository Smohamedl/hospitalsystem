import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HospitalsystemTestModule } from '../../../test.module';
import { ProviderDeleteDialogComponent } from 'app/entities/provider/provider-delete-dialog.component';
import { ProviderService } from 'app/entities/provider/provider.service';

describe('Component Tests', () => {
  describe('Provider Management Delete Component', () => {
    let comp: ProviderDeleteDialogComponent;
    let fixture: ComponentFixture<ProviderDeleteDialogComponent>;
    let service: ProviderService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [ProviderDeleteDialogComponent]
      })
        .overrideTemplate(ProviderDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProviderDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProviderService);
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
