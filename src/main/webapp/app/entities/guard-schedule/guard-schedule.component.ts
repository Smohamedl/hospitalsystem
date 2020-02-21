import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IGuardSchedule } from 'app/shared/model/guard-schedule.model';
import { GuardScheduleService } from './guard-schedule.service';

@Component({
  selector: 'jhi-guard-schedule',
  templateUrl: './guard-schedule.component.html'
})
export class GuardScheduleComponent implements OnInit, OnDestroy {
  guardSchedules: IGuardSchedule[];
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected guardScheduleService: GuardScheduleService,
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
      this.guardScheduleService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IGuardSchedule[]>) => (this.guardSchedules = res.body));
      return;
    }
    this.guardScheduleService.query().subscribe((res: HttpResponse<IGuardSchedule[]>) => {
      this.guardSchedules = res.body;
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
    this.registerChangeInGuardSchedules();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IGuardSchedule) {
    return item.id;
  }

  registerChangeInGuardSchedules() {
    this.eventSubscriber = this.eventManager.subscribe('guardScheduleListModification', () => this.loadAll());
  }
}
