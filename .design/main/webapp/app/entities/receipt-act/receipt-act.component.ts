import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IReceiptAct } from 'app/shared/model/receipt-act.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ReceiptActService } from './receipt-act.service';

import { HttpErrorResponse } from '@angular/common/http';
//eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IMedicalService } from 'app/shared/model/medical-service.model';
import { MedicalServiceService } from 'app/entities/medical-service/medical-service.service';
import { IDoctor } from 'app/shared/model/doctor.model';
import { DoctorService } from 'app/entities/doctor/doctor.service';
import { ICat, Cat } from 'app/shared/model/cat.model';

@Component({
  selector: 'jhi-receipt-act',
  templateUrl: './receipt-act.component.html'
})
export class ReceiptActComponent implements OnInit, OnDestroy {
  receiptActs: IReceiptAct[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  currentSearch: string;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;

  isSaving: boolean;

  medicalservices: IMedicalService[];

  doctors: IDoctor[];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    medicalService: [null, Validators.required],
    doctor: [null, Validators.required]
  });

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,

    protected receiptActService: ReceiptActService,
    protected parseLinks: JhiParseLinks,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected jhiAlertService: JhiAlertService,
    protected medicalServiceService: MedicalServiceService,
    protected doctorService: DoctorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {this.__componentInspectorService.getComp(this);

    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.receiptActService
        .search({
          page: this.page - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe((res: HttpResponse<IReceiptAct[]>) => this.paginateReceiptActs(res.body, res.headers));
      return;
    }
    this.receiptActService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IReceiptAct[]>) => this.paginateReceiptActs(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/receipt-act'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        search: this.currentSearch,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.currentSearch = '';
    this.router.navigate([
      '/receipt-act',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.page = 0;
    this.currentSearch = query;
    this.router.navigate([
      '/receipt-act',
      {
        search: this.currentSearch,
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.isSaving = false;
    this.medicalServiceService
      .query()
      .subscribe(
        (res: HttpResponse<IMedicalService[]>) => (this.medicalservices = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.doctorService
      .query()
      .subscribe((res: HttpResponse<IDoctor[]>) => (this.doctors = res.body), (res: HttpErrorResponse) => this.onError(res.message));

    this.loadAll();
    this.registerChangeInReceiptActs();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IReceiptAct) {
    return item.id;
  }

  registerChangeInReceiptActs() {
    this.eventSubscriber = this.eventManager.subscribe('receiptActListModification', () => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateReceiptActs(data: IReceiptAct[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.receiptActs = data;
  }

  // =======================================

  onChangeService() {
    this.doctorService
      .findByService(this.editForm.get(['medicalService']).value.name)
      .subscribe((res: HttpResponse<IDoctor[]>) => (this.doctors = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(cat: ICat) {
    this.editForm.patchValue({
      id: cat.id,
      medicalService: cat.medicalService,
      doctor: cat.doctor
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.receiptActService
      .findByDoctor(
        {
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        },
        this.editForm.get(['medicalService']).value.name
      )
      .subscribe((res: HttpResponse<IReceiptAct[]>) => this.paginateReceiptActs(res.body, res.headers));
  }

  private createFromForm(): ICat {
    return {
      ...new Cat(),
      id: this.editForm.get(['id']).value,
      medicalService: this.editForm.get(['medicalService']).value,
      doctor: this.editForm.get(['doctor']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICat>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackMedicalServiceById(index: number, item: IMedicalService) {
    return item.id;
  }

  trackDoctorById(index: number, item: IDoctor) {
    return item.id;
  }
}
