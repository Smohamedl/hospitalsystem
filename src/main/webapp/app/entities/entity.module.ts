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
        path: 'guard-schedule',
        loadChildren: () => import('./guard-schedule/guard-schedule.module').then(m => m.HospitalsystemGuardScheduleModule)
      },
      {
        path: 'guard',
        loadChildren: () => import('./guard/guard.module').then(m => m.HospitalsystemGuardModule)
      },
      {
        path: 'hospitalization',
        loadChildren: () => import('./hospitalization/hospitalization.module').then(m => m.HospitalsystemHospitalizationModule)
      },
      {
        path: 'receipt-act',
        loadChildren: () => import('./receipt-act/receipt-act.module').then(m => m.HospitalsystemReceiptActModule)
      },
      {
        path: 'social-organization',
        loadChildren: () => import('./social-organization/social-organization.module').then(m => m.HospitalsystemSocialOrganizationModule)
      },
      {
        path: 'session',
        loadChildren: () => import('./session/session.module').then(m => m.HospitalsystemSessionModule)
      },
      {
        path: 'doctor-monthly-payment',
        loadChildren: () =>
          import('./doctor-monthly-payment/doctor-monthly-payment.module').then(m => m.HospitalsystemDoctorMonthlyPaymentModule)
      },
      {
        path: 'doctor-part-payment',
        loadChildren: () => import('./doctor-part-payment/doctor-part-payment.module').then(m => m.HospitalsystemDoctorPartPaymentModule)
      },
      {
        path: 'social-organization-regimen',
        loadChildren: () =>
          import('./social-organization-regimen/social-organization-regimen.module').then(
            m => m.HospitalsystemSocialOrganizationRegimenModule
          )
      },
      {
        path: 'social-organization-details',
        loadChildren: () =>
          import('./social-organization-details/social-organization-details.module').then(
            m => m.HospitalsystemSocialOrganizationDetailsModule
          )
      },
      {
        path: 'payment-method',
        loadChildren: () => import('./payment-method/payment-method.module').then(m => m.HospitalsystemPaymentMethodModule)
      },
      {
        path: 'stock',
        loadChildren: () => import('./stock/stock.module').then(m => m.HospitalsystemStockModule)
      },
      {
        path: 'product',
        loadChildren: () => import('./product/product.module').then(m => m.HospitalsystemProductModule)
      },
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.HospitalsystemCategoryModule)
      },
      {
        path: 'provider',
        loadChildren: () => import('./provider/provider.module').then(m => m.HospitalsystemProvidedrModule)
      },
      {
        path: 'order',
        loadChildren: () => import('./order/order.module').then(m => m.HospitalsystemOrderModule)
      },
      {
        path: 'quantity-price',
        loadChildren: () => import('./quantity-price/quantity-price.module').then(m => m.HospitalsystemQuantityPriceModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class HospitalsystemEntityModule {}
