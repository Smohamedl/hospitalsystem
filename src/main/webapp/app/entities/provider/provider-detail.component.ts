import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProvidedr } from 'app/shared/model/provider.model';

@Component({
  selector: 'jhi-providedr-detail',
  templateUrl: './provider-detail.component.html'
})
export class ProviderDetailComponent implements OnInit {
  providedr: IProvidedr;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ providedr }) => {
      this.providedr = providedr;
    });
  }

  previousState() {
    window.history.back();
  }
}
