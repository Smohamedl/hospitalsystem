import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { HospitalsystemTestModule } from '../../../test.module';
import { SocialOrganizationDetailsDeleteDialogComponent } from 'app/entities/social-organization-details/social-organization-details-delete-dialog.component';
import { SocialOrganizationDetailsService } from 'app/entities/social-organization-details/social-organization-details.service';

describe('Component Tests', () => {
  describe('SocialOrganizationDetails Management Delete Component', () => {
    let comp: SocialOrganizationDetailsDeleteDialogComponent;
    let fixture: ComponentFixture<SocialOrganizationDetailsDeleteDialogComponent>;
    let service: SocialOrganizationDetailsService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [SocialOrganizationDetailsDeleteDialogComponent]
      })
        .overrideTemplate(SocialOrganizationDetailsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SocialOrganizationDetailsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SocialOrganizationDetailsService);
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
