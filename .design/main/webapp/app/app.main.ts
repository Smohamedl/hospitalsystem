import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { ProdConfig } from './blocks/config/prod.config';
import { HospitalsystemAppModule } from './app.module';

ProdConfig();

if (module['hot']) {
  module['hot'].accept();
}

platformBrowserDynamic()
  .bootstrapModule(HospitalsystemAppModule, { preserveWhitespaces: true })
  .then(() => console.log('Application started'))
  .catch(err => console.error(err));
