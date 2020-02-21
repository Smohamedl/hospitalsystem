import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IGuard } from 'app/shared/model/guard.model';
import { GuardService } from './guard.service';

@Component({
  selector: 'jhi-guard',
  templateUrl: './guard.component.html'
})
export class GuardComponent implements OnInit, OnDestroy {
  guards: IGuard[];
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(protected guardService: GuardService, protected eventManager: JhiEventManager, protected activatedRoute: ActivatedRoute) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.guardService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IGuard[]>) => (this.guards = res.body));
      return;
    }
    this.guardService.query().subscribe((res: HttpResponse<IGuard[]>) => {
      this.guards = res.body;
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
    this.registerChangeInGuards();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IGuard) {
    return item.id;
  }

  registerChangeInGuards() {
    this.eventSubscriber = this.eventManager.subscribe('guardListModification', () => this.loadAll());
  }
}
