import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAct } from 'app/shared/model/act.model';

@Component({
  selector: 'jhi-act-detail',
  templateUrl: './act-detail.component.html'
})
export class ActDetailComponent implements OnInit {
  act: IAct;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ act }) => {
      this.act = act;
    });
  }

  previousState() {
    window.history.back();
  }
}
