import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IActype } from 'app/shared/model/actype.model';

@Component({
  selector: 'jhi-actype-detail',
  templateUrl: './actype-detail.component.html'
})
export class ActypeDetailComponent implements OnInit {
  actype: IActype;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ actype }) => {
      this.actype = actype;
    });
  }

  previousState() {
    window.history.back();
  }
}
