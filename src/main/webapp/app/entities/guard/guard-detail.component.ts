import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGuard } from 'app/shared/model/guard.model';

@Component({
  selector: 'jhi-guard-detail',
  templateUrl: './guard-detail.component.html'
})
export class GuardDetailComponent implements OnInit {
  guard: IGuard;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ guard }) => {
      this.guard = guard;
    });
  }

  previousState() {
    window.history.back();
  }
}
