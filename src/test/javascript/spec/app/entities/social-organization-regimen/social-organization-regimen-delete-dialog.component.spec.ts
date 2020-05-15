import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HospitalsystemTestModule } from '../../../test.module';
import { SocialOrganizationRegimenDeleteDialogComponent } from 'app/entities/social-organization-regimen/social-organization-regimen-delete-dialog.component';
import { SocialOrganizationRegimenService } from 'app/entities/social-organization-regimen/social-organization-regimen.service';

describe('Component Tests', () => {
  describe('SocialOrganizationRegimen Management Delete Component', () => {
    let comp: SocialOrganizationRegimenDeleteDialogComponent;
    let fixture: ComponentFixture<SocialOrganizationRegimenDeleteDialogComponent>;
    let service: SocialOrganizationRegimenService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [SocialOrganizationRegimenDeleteDialogComponent]
      })
        .overrideTemplate(SocialOrganizationRegimenDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SocialOrganizationRegimenDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SocialOrganizationRegimenService);
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
