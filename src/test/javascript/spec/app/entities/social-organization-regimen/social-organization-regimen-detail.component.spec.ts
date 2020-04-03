import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HospitalsystemTestModule } from '../../../test.module';
import { SocialOrganizationRegimenDetailComponent } from 'app/entities/social-organization-regimen/social-organization-regimen-detail.component';
import { SocialOrganizationRegimen } from 'app/shared/model/social-organization-regimen.model';

describe('Component Tests', () => {
  describe('SocialOrganizationRegimen Management Detail Component', () => {
    let comp: SocialOrganizationRegimenDetailComponent;
    let fixture: ComponentFixture<SocialOrganizationRegimenDetailComponent>;
    const route = ({ data: of({ socialOrganizationRegimen: new SocialOrganizationRegimen(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HospitalsystemTestModule],
        declarations: [SocialOrganizationRegimenDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SocialOrganizationRegimenDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SocialOrganizationRegimenDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.socialOrganizationRegimen).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
