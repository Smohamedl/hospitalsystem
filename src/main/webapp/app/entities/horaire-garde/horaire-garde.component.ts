import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IHoraire_garde } from 'app/shared/model/horaire-garde.model';
import { Horaire_gardeService } from './horaire-garde.service';

@Component({
  selector: 'jhi-horaire-garde',
  templateUrl: './horaire-garde.component.html'
})
export class Horaire_gardeComponent implements OnInit, OnDestroy {
  horaire_gardes: IHoraire_garde[];
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected horaire_gardeService: Horaire_gardeService,
    protected eventManager: JhiEventManager,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.horaire_gardeService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IHoraire_garde[]>) => (this.horaire_gardes = res.body));
      return;
    }
    this.horaire_gardeService.query().subscribe((res: HttpResponse<IHoraire_garde[]>) => {
      this.horaire_gardes = res.body;
      this.currentSearch = '';
    });
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.loadAll();
  }

  clear() {
    this.currentSearch = '';
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInHoraire_gardes();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IHoraire_garde) {
    return item.id;
  }

  registerChangeInHoraire_gardes() {
    this.eventSubscriber = this.eventManager.subscribe('horaire_gardeListModification', () => this.loadAll());
  }
}
