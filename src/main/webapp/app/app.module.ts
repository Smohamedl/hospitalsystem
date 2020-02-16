import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { HospitalsystemSharedModule } from 'app/shared/shared.module';
import { HospitalsystemCoreModule } from 'app/core/core.module';
import { HospitalsystemAppRoutingModule } from './app-routing.module';
import { HospitalsystemHomeModule } from './home/home.module';
import { HospitalsystemEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatAutocompleteModule,MatInputModule} from '@angular/material';
import {AutoCompleteModule} from 'primeng/autocomplete';


@NgModule({
  imports: [
    BrowserModule,
    HospitalsystemSharedModule,
    HospitalsystemCoreModule,
    HospitalsystemHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    HospitalsystemEntityModule,
    HospitalsystemAppRoutingModule,
    BrowserAnimationsModule, 
    MatAutocompleteModule, 
    MatInputModule,
    FormsModule, 
    ReactiveFormsModule,
    AutoCompleteModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class HospitalsystemAppModule {}
