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
      },
      {
        path: 'act',
        loadChildren: () => import('./act/act.module').then(m => m.HospitalsystemActModule)
      },
      {
        path: 'horaire-garde',
        loadChildren: () => import('./horaire-garde/horaire-garde.module').then(m => m.HospitalsystemHoraire_gardeModule)
      },
      {
        path: 'guard-schedule',
        loadChildren: () => import('./guard-schedule/guard-schedule.module').then(m => m.HospitalsystemGuardScheduleModule)
      },
      {
        path: 'guard',
        loadChildren: () => import('./guard/guard.module').then(m => m.HospitalsystemGuardModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class HospitalsystemEntityModule {}
