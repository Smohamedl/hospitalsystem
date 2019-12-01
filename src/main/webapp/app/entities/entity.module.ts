import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'patient',
        loadChildren: () => import('./patient/patient.module').then(m => m.HospitalsystemPatientModule)
      },
      {
        path: 'medical-service',
        loadChildren: () => import('./medical-service/medical-service.module').then(m => m.HospitalsystemMedicalServiceModule)
      },
      {
        path: 'doctor',
        loadChildren: () => import('./doctor/doctor.module').then(m => m.HospitalsystemDoctorModule)
      },
      {
        path: 'actype',
        loadChildren: () => import('./actype/actype.module').then(m => m.HospitalsystemActypeModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class HospitalsystemEntityModule {}
