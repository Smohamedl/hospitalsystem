import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProvider } from 'app/shared/model/provider.model';

@Component({
  selector: 'jhi-Provider-detail',
  templateUrl: './provider-detail.component.html'
})
export class ProviderDetailComponent implements OnInit {
  provider: IProvider;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ Provider }) => {
      this.provider = Provider;
    });
  }

  previousState() {
    window.history.back();
  }
}
