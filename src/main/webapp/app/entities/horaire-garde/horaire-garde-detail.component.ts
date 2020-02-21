import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHoraire_garde } from 'app/shared/model/horaire-garde.model';

@Component({
  selector: 'jhi-horaire-garde-detail',
  templateUrl: './horaire-garde-detail.component.html'
})
export class Horaire_gardeDetailComponent implements OnInit {
  horaire_garde: IHoraire_garde;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ horaire_garde }) => {
      this.horaire_garde = horaire_garde;
    });
  }

  previousState() {
    window.history.back();
  }
}
